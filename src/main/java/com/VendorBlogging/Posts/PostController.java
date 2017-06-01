package com.VendorBlogging.Posts;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class PostController extends AbstractController {

    public int createPost(
            String cookie,
            String title,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateString(title, "title");
        this.validateText(content, "content");
        // Initialize model and create the data.
        PostModel eventModel = new PostModel();
        return eventModel.createPost(
            cookie,
            title,
            content
        );
    }
}

