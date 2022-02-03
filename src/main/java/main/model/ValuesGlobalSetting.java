package main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ValuesGlobalSetting {

    @AllArgsConstructor
    @Getter
    public enum Code {

        MULTIUSER_MODE("Многопользовательский режим"),
        POST_PREMODERATION("Премодерация постов"),
        STATISTICS_IS_PUBLIC("Показывать всем статистику блога");

        private final String name;
    }

    @AllArgsConstructor
    @Getter
     public enum Value {
        YES("Да", true),
        NO("Нет", false);

        private final String name;
        private final boolean value;

    }
}
