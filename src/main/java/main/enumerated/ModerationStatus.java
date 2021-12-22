package main.enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ModerationStatus {
    NEW("NEW"),
    ACCEPTED("ACCEPTED"),
    DECLINED("DECLINED");
    private final String name;

  }
