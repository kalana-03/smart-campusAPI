package com.westminster.smartcampusapi.exceptions;

//Part 5.2 - Dependecy validation(422 Unprocessable Entity)
//when client POST a new Sesnor with room ID that not exist this will triggered
//Even though request is syntactically valid but due to missing resourses it becomes invalid
public class LinkedResourceNotFoundException extends RuntimeException {
    public LinkedResourceNotFoundException(String message) {
        super(message);
    }
}