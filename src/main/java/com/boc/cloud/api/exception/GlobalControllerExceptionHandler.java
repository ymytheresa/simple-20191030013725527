package com.boc.cloud.api.exception;

import com.boc.cloud.api.MsgKeys;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import java.util.Locale;

/**
 * 全局异常处理类.
 */
@ControllerAdvice
@RestController
class GlobalControllerExceptionHandler {

    @Autowired
    protected MessageSource messageSource;

    /**
     * Spring内部抛出的参数校验错误(code: 400).
     * <p>
     * 比如在@RequestParam中加了required=true
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class, ServletException.class})
    public ErrorResponse handleIllegalArgumentException(Exception e) {
        return new ErrorResponse(MsgKeys.ERROR_INVALID_INPUT, e.getMessage());
    }

    /**
     * Spring内部類型轉換錯誤, 比如String轉Double失敗(HttpMessageNotReadableException)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TypeMismatchException.class)
    public ErrorResponse handleSpringConversionFailedException(Exception e) {
        e.printStackTrace();
        return new ErrorResponse(MsgKeys.ERROR_INVALID_INPUT, getMessage(MsgKeys.IS_INVALID, "input values"));
    }

    /**
     * Spring内部類型轉換錯誤, 比如String轉Double失敗(HttpMessageNotReadableException)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleSpringHttpMessageNotReadableException(Exception e) {
        e.printStackTrace();
        return new ErrorResponse(MsgKeys.ERROR_INVALID_INPUT, getMessage(MsgKeys.IS_INVALID, "input values"));
    }

    /**
     * 自定义的Http400Exception异常(code: 400, 消息内容需要在messages.properties中设定)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Http400Exception.class)
    public ErrorResponse handleClientError(Http400Exception e) {
        return new ErrorResponse(e.getErrorCode(), getMessage(e.getMsgKey(), e.getArgs()));
    }

    /**
     * 自定义的Http401Exception异常(code: 401, 消息内容需要在messages.properties中设定)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Http401Exception.class)
    public ErrorResponse handleAuthorizationError(Http401Exception e) {
        return new ErrorResponse(e.getErrorCode(), getMessage(e.getMsgKey(), e.getArgs()));
    }

    /**
     * 自定义的Http403Exception异常(code: 403, 消息内容需要在messages.properties中设定)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(Http403Exception.class)
    public ErrorResponse handleForbiddenError(Http403Exception e) {
        return new ErrorResponse(e.getErrorCode(), getMessage(e.getMsgKey(), e.getArgs()));
    }

    /**
     * 自定义的Http404Exception异常(code: 404, 消息内容需要在messages.properties中设定)
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Http404Exception.class)
    public ErrorResponse handleForbiddenError(Http404Exception e) {
        return new ErrorResponse(e.getErrorCode(), getMessage(e.getMsgKey(), e.getArgs()));
    }

    /**
     * 其它未捕获的系统异常(code: 500).
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleInternalServerError(Exception e) {
        e.printStackTrace();
        //return new ErrorResponse(MsgKeys.ERROR_INTERNAL, getMessage(MsgKeys.INTERNAL_SERVER_ERROR_WITHSTACKTRACE, e.getMessage() != null ? e.getMessage() : ExceptionUtils.getStackTrace(e)));
        return new ErrorResponse(MsgKeys.ERROR_INTERNAL, getMessage(MsgKeys.INTERNAL_SERVER_ERROR));
    }

    /**
     * get i18n message
     *
     * @param code message key
     * @param args parameters used in the message key.
     * @return
     */
    private String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.US);
    }

}

