package com.epam.library.command;

import com.epam.library.domain.Request;
import com.epam.library.domain.Response;

/**
 * Created by test on 19.03.2017.
 */
public interface Command {
    Response execute(Request request);
}
