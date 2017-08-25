package com.dingli.diandians.newProject.moudle.eye.protocol;

import java.util.List;

/**
 * Created by lwq on 2017/6/6.
 */
public class MessageCommentListProtocl {
    public int totalCount;
    public int pageCount;
    public int offset;
    public int limit;
    public List<CommentProtocol> data;
}
