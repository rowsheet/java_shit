package com.VendorBlogging;

import com.VendorBlogging.Posts.PostController;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by alexanderkleinhans on 5/27/17.
 */
@WebService
public class VendorBloggingHandler {
    @WebMethod
    public String Testing(
            @WebParam(name="something") String something
    ) {
        return "VALUE:" + something;
    }

    @WebMethod
    public int createPost(
            @WebParam(name="cookie") String cookie,
            @WebParam(name="title") String title,
            @WebParam(name="content") String content
    ) throws Exception {
        PostController postController = new PostController();
        return postController.createPost(
                cookie,
                title,
                content
        );
    }
}
