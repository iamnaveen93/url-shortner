package in.practice.urlshortner.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Error {

    private int errorCode;
    private String errorMessage;
}
