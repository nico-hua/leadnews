/*
 Navicat Premium Data Transfer

 Source Server         : mongo
 Source Server Type    : MongoDB
 Source Server Version : 80004 (8.0.4)
 Source Host           : localhost:27017
 Source Schema         : leadnews-comment

 Target Server Type    : MongoDB
 Target Server Version : 80004 (8.0.4)
 File Encoding         : 65001

 Date: 25/01/2025 11:40:24
*/


// ----------------------------
// Collection structure for ap_comments
// ----------------------------
db.getCollection("ap_comments").drop();
db.createCollection("ap_comments");

// ----------------------------
// Documents of ap_comments
// ----------------------------
db.getCollection("ap_comments").insert([ {
    _id: ObjectId("6790b7937a8ca77c2c1dae09"),
    authorId: NumberInt("4"),
    authorName: "admin",
    entryId: NumberLong("1880191961529454593"),
    type: NumberInt("0"),
    content: "你好，世界！",
    image: "http://127.0.0.1:9000/leadnews/2025/01/12/2adfc2b6305c049dbdfa98d34065d47.jpg",
    likes: NumberInt("79"),
    reply: NumberInt("2"),
    flag: NumberInt("1"),
    createdTime: NumberLong("1737537427427"),
    _class: "com.heima.comment.pojos.ApComment"
} ]);
db.getCollection("ap_comments").insert([ {
    _id: ObjectId("6790be46c59eaa7b7133ffb5"),
    authorId: NumberInt("4"),
    authorName: "admin",
    entryId: NumberLong("1880191961529454593"),
    type: NumberInt("0"),
    content: "2333下次一定",
    image: "http://127.0.0.1:9000/leadnews/2025/01/12/2adfc2b6305c049dbdfa98d34065d47.jpg",
    likes: NumberInt("5"),
    reply: NumberInt("3"),
    flag: NumberInt("1"),
    createdTime: NumberLong("1737539142659"),
    _class: "com.heima.comment.pojos.ApComment"
} ]);

// ----------------------------
// Collection structure for ap_repays
// ----------------------------
db.getCollection("ap_repays").drop();
db.createCollection("ap_repays");

// ----------------------------
// Documents of ap_repays
// ----------------------------
db.getCollection("ap_repays").insert([ {
    _id: ObjectId("6790c5440939cb197ae6369e"),
    commentId: "6790be46c59eaa7b7133ffb5",
    authorId: NumberInt("4"),
    authorName: "admin",
    type: NumberInt("0"),
    content: "你好",
    image: "http://127.0.0.1:9000/leadnews/2025/01/12/2adfc2b6305c049dbdfa98d34065d47.jpg",
    likes: NumberInt("11"),
    flag: NumberInt("1"),
    createdTime: NumberLong("1737540932124"),
    _class: "com.heima.comment.pojos.ApRepay"
} ]);
db.getCollection("ap_repays").insert([ {
    _id: ObjectId("6790c56d0939cb197ae6369f"),
    commentId: "6790be46c59eaa7b7133ffb5",
    authorId: NumberInt("4"),
    authorName: "admin",
    type: NumberInt("0"),
    content: "非常糟糕",
    image: "http://127.0.0.1:9000/leadnews/2025/01/12/2adfc2b6305c049dbdfa98d34065d47.jpg",
    likes: NumberInt("68"),
    flag: NumberInt("1"),
    createdTime: NumberLong("1737540973146"),
    _class: "com.heima.comment.pojos.ApRepay"
} ]);
db.getCollection("ap_repays").insert([ {
    _id: ObjectId("6791b5344bd5da4a812bcf9a"),
    commentId: "6790be46c59eaa7b7133ffb5",
    authorId: NumberInt("1102"),
    authorName: "admin",
    type: NumberInt("0"),
    content: "别下次，就现在吧",
    likes: NumberInt("0"),
    createdTime: NumberLong("1737602356491"),
    _class: "com.heima.wemedia.pojos.ApRepay"
} ]);
db.getCollection("ap_repays").insert([ {
    _id: ObjectId("6791b5654bd5da4a812bcf9b"),
    commentId: "6790b7937a8ca77c2c1dae09",
    authorId: NumberInt("1102"),
    authorName: "admin",
    type: NumberInt("0"),
    content: "你好",
    likes: NumberInt("0"),
    createdTime: NumberLong("1737602405005"),
    _class: "com.heima.wemedia.pojos.ApRepay"
} ]);
db.getCollection("ap_repays").insert([ {
    _id: ObjectId("6791bafd1de71e0f9e6efce5"),
    commentId: "6790b7937a8ca77c2c1dae09",
    authorId: NumberInt("1102"),
    authorName: "admin",
    type: NumberInt("0"),
    content: "好好好",
    likes: NumberInt("0"),
    createdTime: NumberLong("1737603837037"),
    _class: "com.heima.wemedia.pojos.ApRepay"
} ]);
