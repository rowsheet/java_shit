package com.UserVendorCommunication.Blogs;

import com.Common.AbstractController;

/**
 * Created by alexanderkleinhans on 6/1/17.
 */
public class BlogController extends AbstractController {

    public int createBlogPostComment(
            String cookie,
            int vendor_post_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_post_id, "post_id");
        this.validateText(content, "content");
        // Initialize model and create the data.
        BlogModel blogModel = new BlogModel();
        return blogModel.createBlogPostComment(
                cookie,
                vendor_post_id,
                content
        );
    }

    public int createBlogPostCommentReply(
            String cookie,
            int vendor_post_comment_id,
            String content
    ) throws Exception {
        // Validate input parameters.
        this.validateString(cookie, "cookie");
        this.validateID(vendor_post_comment_id, "post_id");
        this.validateText(content, "content");
        // Initialize model and create the data.
        BlogModel blogModel = new BlogModel();
        return blogModel.createBlogPostCommentReply(
                cookie,
                vendor_post_comment_id,
                content
        );
    }
}

