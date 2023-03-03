package com.touchbiz.common.utils.minio;

import com.touchbiz.common.utils.minio.configuration.CustomMinioClient;
import com.touchbiz.common.utils.minio.configuration.MinioConfig;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.management.openmbean.InvalidKeyException;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Component
public class MinioUtils {

    private static MinioClient minioClient;

    private static MinioClient splitClient;

    private static final String SEPARATOR = "/";

    public static final String CONTENT_TYPE_IMAGE = "image/png";

    /**
     * 排序
     */
    public final static boolean SORT = true;
    /**
     * 不排序
     */
    public final static boolean NOT_SORT = false;
    /**
     * 默认过期时间(分钟)
     */
    private final static Integer DEFAULT_EXPIRY = 1440;

    private final MinioConfig minioConfig;

    public MinioUtils(MinioConfig minioConfig) {
        try {
            var builder = MinioClient.builder()
                    .endpoint(minioConfig.getUrl())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey());
            if (!ObjectUtils.isEmpty(minioConfig.getRegion())) {
                builder.region(minioConfig.getRegion());
            }
            minioClient = builder.build();
        } catch (Exception e) {
            log.error("minioClient err:", e);
        }
        try {
            splitClient = new CustomMinioClient(MinioClient.builder()
                    .endpoint(minioConfig.getExternalUrl())
                    .region(minioConfig.getRegion())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                    .build());
        } catch (Exception err) {
            log.error("splitClient err:", err);
        }
        this.minioConfig = minioConfig;
    }

    /**
     * 获取上传文件的基础路径
     *
     * @return url
     */
    public static String getBasisUrl(String bucketName) {
        return SEPARATOR + bucketName + SEPARATOR;
    }

    /**
     * 根据URL获取objectname
     *
     * @param filePath
     * @return
     */
    public static String getObjectName(String filePath, String minioUrl) {
        String objectName;
        String out = filePath.replace(minioUrl + "/", "");
        int first = out.indexOf("/");
        objectName = out.substring(first + 1);
        return objectName;
    }

    /**
     * 获取文件夹名称
     *
     * @param filePath
     * @return
     */
    public static String getObjectDir(String filePath, String minioUrl) {
        String objectName = getObjectName(filePath, minioUrl);
        return objectName.split("/")[0];
    }

    /**
     * 获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getObjectRealName(String filePath, String minioUrl) {
        String objectName = getObjectName(filePath, minioUrl);
        return objectName.split("/")[1];
    }

    /**
     * 根据URL获取bucketname
     *
     * @param filePath
     * @return
     */
    public static String getBucketName(String filePath, String minioUrl) {
        String bucketName;
        String out = filePath.replace(minioUrl + "/", "");
        int first = out.indexOf("/");
        bucketName = out.substring(0, first);
        return bucketName;
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     */
    public static List<Bucket> getAllBuckets()
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        return minioClient.listBuckets();
    }


    /**
     * 获取文件夹名称
     *
     * @return
     */
    public static String getDictionaryName() {
        var now = LocalDate.now();
        return String.valueOf(now.getYear() * 10000 + now.getMonthValue() * 100 + now.getDayOfMonth());
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean isBucketExist(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean createBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    /**
     * 获取访问对象的外链地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry     过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return viewUrl
     */
    @SneakyThrows
    public static String getObjectUrl(String bucketName, String objectName, Integer expiry) {
        expiry = expiryHandle(expiry);
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expiry)
                        .build()
        );
    }

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @param expiry     过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return uploadUrl
     */
    @SneakyThrows
    public static String createUploadUrl(String bucketName, String objectName, Integer expiry) {
        expiry = expiryHandle(expiry);
        return splitClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expiry)
                        .build()
        );
    }

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @return uploadUrl
     */
    public static String createUploadUrl(String bucketName, String objectName) {
        return createUploadUrl(bucketName, objectName, DEFAULT_EXPIRY);
    }

    /**
     * 批量创建分片上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param chunkCount 分片数量
     * @return uploadChunkUrls
     */
    public static List<String> createUploadChunkUrlList(String bucketName, String objectMD5, Integer chunkCount) {
        if (null == bucketName) {
            return null;
        }
        if (null == objectMD5) {
            return null;
        }
        objectMD5 += "/";
        if (null == chunkCount || 0 == chunkCount) {
            return null;
        }
        List<String> urlList = new ArrayList<>(chunkCount);
        for (int i = 1; i <= chunkCount; i++) {
            String objectName = objectMD5 + i + ".chunk";
            urlList.add(createUploadUrl(bucketName, objectName, DEFAULT_EXPIRY));
        }
        return urlList;
    }

    /**
     * 创建指定序号的分片文件上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param partNumber 分片序号
     * @return uploadChunkUrl
     */
    public static String createUploadChunkUrl(String bucketName, String objectMD5, Integer partNumber) {
        if (null == bucketName) {
            return null;
        }
        if (null == objectMD5) {
            return null;
        }
        objectMD5 += "/" + partNumber + ".chunk";
        return createUploadUrl(bucketName, objectMD5, DEFAULT_EXPIRY);
    }

    /**
     * 获取对象文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀
     * @param sort       是否排序(升序)
     * @return objectNames
     */
    @SneakyThrows
    public static List<String> listObjectNames(String bucketName, String prefix, Boolean sort) {
        ListObjectsArgs listObjectsArgs;
        if (null == prefix) {
            listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build();
        } else {
            listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .recursive(true)
                    .build();
        }
        Iterable<Result<Item>> chunks = minioClient.listObjects(listObjectsArgs);
        List<String> chunkPaths = new ArrayList<>();
        for (Result<Item> item : chunks) {
            chunkPaths.add(item.get().objectName());
        }
        if (sort) {
            return chunkPaths.stream().distinct().collect(Collectors.toList());
        }
        return chunkPaths;
    }

    /**
     * 获取对象文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀
     * @return objectNames
     */
    public static List<String> listObjectNames(String bucketName, String prefix) {
        if (null == bucketName) {
            return null;
        }
        return listObjectNames(bucketName, prefix, NOT_SORT);
    }

    /**
     * 获取分片文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param ObjectMd5  对象Md5
     * @return objectChunkNames
     */
    public static List<String> listChunkObjectNames(String bucketName, String ObjectMd5) {
        if (null == bucketName) {
            return null;
        }
        if (null == ObjectMd5) {
            return null;
        }
        return listObjectNames(bucketName, ObjectMd5, SORT);
    }

    /**
     * 获取分片名称地址HashMap key=分片序号 value=分片文件地址
     *
     * @param bucketName 存储桶名称
     * @param ObjectMd5  对象Md5
     * @return objectChunkNameMap
     */
    public static Map<Integer, String> mapChunkObjectNames(String bucketName, String ObjectMd5) {
        if (null == bucketName) {
            return null;
        }
        if (null == ObjectMd5) {
            return null;
        }
        List<String> chunkPaths = listObjectNames(bucketName, ObjectMd5);
        if (null == chunkPaths || chunkPaths.size() == 0) {
            return null;
        }
        Map<Integer, String> chunkMap = new HashMap<>(chunkPaths.size());
        for (String chunkName : chunkPaths) {
            Integer partNumber = Integer.parseInt(chunkName.substring(chunkName.indexOf("/") + 1, chunkName.lastIndexOf(".")));
            chunkMap.put(partNumber, chunkName);
        }
        return chunkMap;
    }

    /**
     * 合并分片文件成对象文件
     *
     * @param chunkBucKetName   分片文件所在存储桶名称
     * @param composeBucketName 合并后的对象文件存储的存储桶名称
     * @param chunkNames        分片文件名称集合
     * @param objectName        合并后的对象文件名称
     * @return true/false
     */
    @SneakyThrows
    public static boolean composeObject(String chunkBucKetName, String composeBucketName, List<String> chunkNames, String objectName) {
        if (null == chunkBucKetName) {
            return false;
        }
        List<ComposeSource> sourceObjectList = new ArrayList<>(chunkNames.size());
        for (String chunk : chunkNames) {
            sourceObjectList.add(
                    ComposeSource.builder()
                            .bucket(chunkBucKetName)
                            .object(chunk)
                            .build()
            );
        }
        Map<String, String> headerMap = new HashMap<>(1);
        headerMap.put("Content-Type", "video/mp4");
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(composeBucketName)
                        .object(objectName)
                        .headers(headerMap)
                        .sources(sourceObjectList)
                        .build()
        );
        return true;
    }

    /**
     * 将分钟数转换为秒数
     *
     * @param expiry 过期时间(分钟数)
     * @return expiry
     */
    private static int expiryHandle(Integer expiry) {
        expiry = expiry * 60;
        if (expiry > 604800) {
            return 604800;
        }
        return expiry;
    }

    /**
     * 通过MultipartFile，上传文件
     *
     * @param bucketName 存储桶
     * @param file       文件
     * @param objectName 对象名
     */
    public static ObjectWriteResponse putObject(String bucketName, MultipartFile file,
                                                String objectName, String contentType)
            throws IOException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        InputStream inputStream = file.getInputStream();
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(contentType)
                        .stream(
                                inputStream, inputStream.available(), -1)
                        .build());
    }

    public static ObjectWriteResponse putObject(String bucketName, String objectName, String contentType,
                                                InputStream inputStream)
            throws IOException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                inputStream, inputStream.available(), -1)
                        .contentType(contentType)
                        .build());
    }

    public static ObjectWriteResponse putObjectMultipart(String bucketName, File file,
                                                         String objectName, String contentType, Map<String, String> map)
            throws IOException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        InputStream inputStream = new FileInputStream(file);
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(contentType)
                        .headers(map)
                        .stream(
                                inputStream, inputStream.available(), inputStream.available())
                        .build());
    }

    public static ObjectWriteResponse putObject(String bucketName, File file,
                                                String objectName, String contentType)
            throws IOException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        InputStream inputStream = new FileInputStream(file);
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).contentType(contentType)
                        .stream(
                                inputStream, inputStream.available(), -1)
                        .build());
    }

    /**
     * 上传本地文件
     *
     * @param bucketName 存储桶
     * @param objectName 对象名称
     * @param fileName   本地文件路径
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName,
                                                String fileName)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        return minioClient.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucketName).object(objectName).filename(fileName).build());
    }

    /**
     * 通过流上传文件
     *
     * @param bucketName  存储桶
     * @param objectName  文件对象
     * @param inputStream 文件流
     */
    public static ObjectWriteResponse putObject(String bucketName, String objectName,
                                                InputStream inputStream)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                inputStream, inputStream.available(), -1)
                        .build());
    }

    public static String getFilePath(ObjectWriteResponse response) {
        return "/" + response.bucket() + "/" + response.object();
    }

    public static String url(String bucketName, String obj) {
        return "/" + bucketName + "/" + obj;
    }

    /**
     * 获取文件路径（兼容minio以及fastdfs）
     *
     * @param url 数据库中的URL
     * @throws IOException 抛IO异常
     */
    public static String replaceExternalString(String url, String externalUrl) {
        if (ObjectUtils.isEmpty(url) || url.startsWith("http")) {
            return url;
        }
        if (!StringUtils.isEmpty(externalUrl)) {
            return externalUrl + url;
        }
        return url;
    }

    public String replaceExternalString(String url) {
        if (ObjectUtils.isEmpty(url)) {
            return url;
        }
        if (url.startsWith("/")) {
            String minioUrl = minioConfig.getUrl();
            if (minioUrl.endsWith("/")) {
                return minioUrl.substring(0, minioUrl.length() - 1) + url;

            }
            return minioUrl + url;
        }
        return url;
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     */
    public static void removeObject(String bucketName, String objectName)
            throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, java.security.InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        minioClient
                .removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    /**
     * 拷贝文件
     *
     * @param bucketName    bucket名称
     * @param objectName    文件名称
     * @param srcBucketName 目标bucket名称
     * @param srcObjectName 目标文件名称
     */
    @SneakyThrows
    public static ObjectWriteResponse copyObject(String bucketName, String objectName,
                                                 String srcBucketName, String srcObjectName)
            throws InvalidKeyException {
        return minioClient.copyObject(
                CopyObjectArgs.builder()
                        .source(CopySource.builder().bucket(bucketName).object(objectName).build())
                        .bucket(srcBucketName)
                        .object(srcObjectName)
                        .build());
    }

    /**
     * 创建文件夹或目录
     *
     * @param bucketName 存储桶
     * @param objectName 目录路径
     */
    public static ObjectWriteResponse putDirObject(String bucketName, String objectName)
            throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException, java.security.InvalidKeyException {
        return minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                new ByteArrayInputStream(new byte[]{}), 0, -1)
                        .build());
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 桶名称
     * @param objectName 文件名称, 如果要带文件夹请用 / 分割, 例如 /help/index.html
     * @return true存在, 反之
     */
    public static Boolean checkFileIsExist(String bucketName, String objectName) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 文件下载
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件路径
     * @return
     */
    @SneakyThrows
    public static InputStream getObject(String bucketName, String fileName) {
        //桶是否存在
        boolean flag = isBucketExist(bucketName);
        InputStream stream = null;
        if (flag) {
            StatObjectResponse objectStat = minioClient
                    .statObject(StatObjectArgs.builder().bucket(bucketName).object(fileName).build());
            //文件存在
            if (objectStat != null) {
                stream = minioClient.getObject(
                        GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .build());
            }
        }
        return stream;
    }


}
