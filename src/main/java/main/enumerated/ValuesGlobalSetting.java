package main.enumerated;

import lombok.AllArgsConstructor;

public class ValuesGlobalSetting {

    @AllArgsConstructor
    public enum Code {

        MULTIUSER_MODE("Многопользовательский режим"),
        POST_PREMODERATION("Премодерация постов"),
        STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

        private final String name;

        public String getName() {
            return name;
        }

    }

    @AllArgsConstructor
    public enum Value {
        YES("Да", true),
        NO("Нет", false);

        private final String name;
        private final boolean value;


        public String getName() {
            return name;
        }

        public boolean getValue() {
            return value;
        }


    }
}
