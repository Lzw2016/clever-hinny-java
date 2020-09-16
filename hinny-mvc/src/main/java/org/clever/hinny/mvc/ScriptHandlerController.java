package org.clever.hinny.mvc;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/16 21:58 <br/>
 */
public class ScriptHandlerController extends MarkController {
    private final ScriptHandler scriptHandler;

    public ScriptHandlerController(ScriptHandler scriptHandler) {
        this.scriptHandler = scriptHandler;
    }

    @RequestMapping("/**")
    public void scriptHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        scriptHandler.handle(request, response);
    }
}
