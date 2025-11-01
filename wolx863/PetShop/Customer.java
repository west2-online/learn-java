package org.example;
import java.time.LocalDate;

    public class Customer {
        private String name;
        private int visitCount;
        private LocalDate latestVisitDate;

        public Customer(String name, int visitCount, LocalDate latestVisitDate) {
            this.name = name;
            this.visitCount = visitCount;
            this.latestVisitDate = latestVisitDate;
        }
        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "顾客{" +
                    "名字='" + name + '\'' +
                    ", 到店次数=" + visitCount +
                    ", 最新到店时间=" + latestVisitDate +
                    '}';
        }

        public LocalDate getLatestVisitDate() {
            return latestVisitDate;
        }
    }

