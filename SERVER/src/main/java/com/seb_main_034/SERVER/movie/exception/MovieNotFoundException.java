package com.seb_main_034.SERVER.movie.exception;

/**
 * 데이터베이스에서 영화를 찾지 못했을 때 발생하는 예외.
 */
public class MovieNotFoundException extends RuntimeException {

    /**
     * 영화의 ID를 알고 있을 때 이 생성자를 사용합니다.
     *
     * @param id 찾지 못한 영화의 ID
     */
    public MovieNotFoundException(Long id) {
        super("ID가 " + id + "인 영화를 찾을 수 없습니다.");
    }

    /**
     * 커스텀 메시지를 제공하려면 이 생성자를 사용합니다.
     *
     * @param message 커스텀 메시지
     */
    public MovieNotFoundException(String message) {
        super(message);
    }
}
