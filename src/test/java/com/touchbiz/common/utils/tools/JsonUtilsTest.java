package com.touchbiz.common.utils.tools;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.junit.Test;

import java.time.LocalDateTime;


public class JsonUtilsTest {

    @Test
    public void deseralizze(){
        LocalDateTime date = LocalDateTime.now();
        A a = new A();
        a.setAaa("2222");
        a.setTime(date);
        String json = JsonUtils.toJson(a);
        A b = JsonUtils.fromJson(json,A.class);

    }

    public static class A{
        private LocalDateTime time;

        @JsonIgnore
        private String aaa;

        public LocalDateTime getTime() {
            return time;
        }

        public void setTime(LocalDateTime time) {
            this.time = time;
        }

        public String getAaa() {
            return aaa;
        }

        public void setAaa(String aaa) {
            this.aaa = aaa;
        }
    }
}