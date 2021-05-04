package com.example.swapiassessment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonList {

        // generated using https://www.jsonschema2pojo.org/

        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("next")
        @Expose
        private String next;
        @SerializedName("previous")
        @Expose
        private Object previous;
        @SerializedName("results")
        @Expose
        private List<Person> people = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }

        public Object getPrevious() {
            return previous;
        }

        public void setPrevious(Object previous) {
            this.previous = previous;
        }

        public List<Person> getPeople() {
            return people;
        }

        public void setResults(List<Person> results) {
            this.people = results;
        }

}
