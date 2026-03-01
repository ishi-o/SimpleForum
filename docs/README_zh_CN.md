# `Forumx`

## 项目概述

一个类似百度贴吧的微服务论坛后端系统，基于Spring Cloud Alibaba构建，支持用户管理、内容发布、实时互动、全文搜索、个性化推荐等核心功能。

## 核心业务需求

- **用户体系**：注册登录、个人资料、关注关系、兴趣标签
- **内容管理**：发帖、评论、点赞、收藏
- **Feed流**：关注Feed、个性化推荐Feed
- **实时互动**：在线推送、收件箱、未读计数
- **搜索功能**：全文检索、搜索建议、热搜榜单
- **文件管理**：图片上传、缩略图处理
- **推荐系统**：多路召回、CTR预估、多样性排序

## 技术栈

| 组件           | 技术选型                      | 用途                 |
| -------------- | ----------------------------- | -------------------- |
| 服务框架       | Spring Boot 3.5.x             | 应用基础框架         |
| 微服务架构     | Spring Cloud Alibaba 2025.0.x | 微服务解决方案       |
| 服务注册与配置 | Nacos                         | 服务发现、配置管理   |
| 服务网关       | Spring Cloud Gateway          | 路由、鉴权、限流     |
| ORM框架        | MyBatis                       | 数据库访问           |
| 数据库         | MySQL 8.0                     | 业务数据存储         |
| 缓存           | Redis 7.x                     | 缓存、计数、收件箱   |
| 消息队列       | RocketMQ                      | 异步解耦、最终一致性 |
| 搜索引擎       | Elasticsearch                 | 全文检索             |
| 对象存储       | MinIO                         | 图片、文件存储       |
| 认证授权       | Keycloak                      | OAuth2/JWT认证       |
| 实时推送       | SSE                           | 通知实时推送         |
| AI推荐         | LangChain4j + EasyRec         | 个性化推荐           |
| 容器化         | Docker + Docker Compose       | 部署编排             |

## 模块设计

```
forumx
├── gateway                     # 网关模块 - 路由转发、统一鉴权
├── user-service                # 用户服务 - 资料、关注、标签
├── content-service             # 内容服务 - 帖子、评论、收藏、Feed
├── interaction-service         # 互动服务 - 点赞、通知、收件箱、SSE推送
├── search-service              # 搜索服务 - 全文检索、索引同步
├── file-service                # 文件服务 - 上传下载、图片处理
├── recommend-service           # 推荐服务 - 召回、排序、特征工程
├── commons                     # 公共模块 - DTO、常量、工具类、异常码
├── api                         # API定义模块 - Feign客户端接口
└── deploy                      # 部署配置 - docker-compose
```

## 核心功能实现

### 1. 用户服务 (user-service)

| 接口分类     | 功能点                        | 说明                               |
| ------------ | ----------------------------- | ---------------------------------- |
| **用户资料** | 获取/更新资料、头像管理       | Keycloak负责认证，服务只存业务资料 |
| **关注管理** | 关注/取消、粉丝列表、关注列表 | Redis缓存粉丝关系，MySQL持久化     |
| **用户标签** | 标签获取/更新、热门标签       | 用于推荐系统的用户画像             |
| **用户设置** | 隐私设置、通知设置            | 个性化配置                         |
| **统计**     | 关注数、粉丝数、互动统计      | 实时计数，Redis缓存                |

### 2. 内容服务 (content-service)

| 接口分类     | 功能点                       | 说明                            |
| ------------ | ---------------------------- | ------------------------------- |
| **帖子管理** | 发帖、删帖、详情、列表       | 图片异步上传MinIO，生成缩略图   |
| **Feed流**   | 关注Feed、推荐Feed、探索发现 | 推拉结合模式，普通用户推、大V拉 |
| **评论管理** | 发表评论、评论树、回复列表   | 评论树结构，支持无限层级        |
| **收藏管理** | 收藏/取消、收藏夹、移动      | 每个用户可建多个收藏夹          |
| **标签**     | 标签列表、标签下帖子         | 内容标签化，便于检索            |

### 3. 互动服务 (interaction-service)

| 接口分类     | 功能点                        | 说明                        |
| ------------ | ----------------------------- | --------------------------- |
| **点赞管理** | 点赞/取消、批量状态、点赞列表 | Redis原子计数，异步落库     |
| **实时推送** | SSE连接、未读计数、通知列表   | 多实例通过Redis Pub/Sub转发 |
| **收件箱**   | 三层存储、标记已读、清空      | 热(List)+温(ZSet)+冷(MySQL) |
| **互动记录** | 点赞历史、评论历史、@提及     | 用户所有互动行为的流水      |

### 4. 搜索服务 (search-service)

| 接口分类     | 功能点                                 | 说明                  |
| ------------ | -------------------------------------- | --------------------- |
| **搜索**     | 帖子搜索、用户搜索、评论搜索、高级搜索 | 多字段权重，IK分词    |
| **搜索建议** | 标题补全、标签建议、搜索历史           | 基于ES completion类型 |
| **热搜**     | 热搜词、热搜帖子、热搜用户             | 基于搜索频次统计      |

### 5. 文件服务 (file-service)

| 接口分类 | 功能点                           | 说明                   |
| -------- | -------------------------------- | ---------------------- |
| **上传** | 单文件、批量、图片处理、分片上传 | 格式校验、压缩、缩略图 |
| **访问** | 下载、预览、文件信息、签名URL    | 防盗链                 |
| **管理** | 删除、批量删除、文件列表         | 定时清理未使用文件     |

### 6. 推荐服务 (recommend-service)

| 接口分类     | 功能点                       | 说明              |
| ------------ | ---------------------------- | ----------------- |
| **Feed推荐** | 个性化推荐、刷新、推荐解释   | 多路召回+排序模型 |
| **用户推荐** | 关注推荐、相似用户           | 基于协同过滤      |
| **内容推荐** | 相似帖子、相关帖子、标签推荐 | 基于内容向量      |
| **行为反馈** | 喜欢/不喜欢、隐藏、举报      | 用于模型迭代      |

## Redis 数据结构设计

```
# 关注Feed
feed:inbox:{userId}          ZSet    帖子ID, 时间戳      收件箱（普通用户推送）
feed:outbox:{authorId}       ZSet    帖子ID, 时间戳      发件箱（大V自己发的）

# 互动收件箱
inbox:hot:{userId}           List    通知对象            最近50条
inbox:warm:{userId}          ZSet    通知ID, 时间戳      最近1000条ID
inbox:unread:{userId}        String  整数                未读计数

# 推送连接
push:connections             Hash    userId -> instanceId 用户连接映射

# 点赞
like:status:{postId}         Set     用户ID              点赞状态
like:count:{postId}          String  整数                点赞计数

# 统计
user:stats:{userId}          Hash    关注数/粉丝数/帖子数 用户统计
post:stats:{postId}          Hash    点赞数/评论数/浏览数 帖子统计

# 缓存
user:profile:{userId}        String  用户对象            用户资料缓存
post:{postId}                String  帖子对象            帖子缓存
```

## RocketMQ Topic 设计

| Topic             | 用途         | 生产者   | 消费者   | 说明           |
| ----------------- | ------------ | -------- | -------- | -------------- |
| feed-fanout-topic | Feed推流     | 内容服务 | 互动服务 | 粉丝收件箱写入 |
| like-topic        | 点赞异步落库 | 互动服务 | 互动服务 | 点赞记录持久化 |
| comment-topic     | 评论异步处理 | 互动服务 | 互动服务 | 评论计数更新   |
| inbox-write-topic | 收件箱写入   | 互动服务 | 互动服务 | 异步写收件箱   |
| post-sync-topic   | 帖子同步ES   | 内容服务 | 搜索服务 | 触发索引更新   |
| user-sync-topic   | 用户同步     | Keycloak | 用户服务 | 用户资料创建   |
| file-audit-topic  | 文件审核     | 文件服务 | 审核服务 | 图片鉴黄       |
| user-action-topic | 用户行为     | 各服务   | 推荐服务 | 行为日志收集   |

## API 接口总览

所有接口统一前缀：`/api/v1`，返回格式统一为 `ResultDTO<T>`

### 用户服务 `/users`

| 方法   | 路径                 | 请求参数                             | 返回                     | 说明             |
| ------ | -------------------- | ------------------------------------ | ------------------------ | ---------------- |
| GET    | /profile/{userId}    | `String userId`                      | `UserProfileDTO`         | 获取用户资料     |
| PUT    | /profile             | `UpdateProfileDTO`                   | `UserProfileDTO`         | 更新资料         |
| POST   | /avatar              | `MultipartFile file`                 | `AvatarDTO`              | 上传头像         |
| DELETE | /avatar              | -                                    | `Void`                   | 删除头像         |
| POST   | /follow/{targetId}   | `String targetId`                    | `FollowResultDTO`        | 关注用户         |
| DELETE | /unfollow/{targetId} | `String targetId`                    | `FollowResultDTO`        | 取消关注         |
| GET    | /followers/{userId}  | `String userId, PageRequestDTO page` | `PageDTO<UserFollowDTO>` | 粉丝列表         |
| GET    | /following/{userId}  | `String userId, PageRequestDTO page` | `PageDTO<UserFollowDTO>` | 关注列表         |
| POST   | /follow/status       | `FollowStatusRequestDTO`             | `Map<String, Boolean>`   | 批量查询关注状态 |
| GET    | /tags/{userId}       | `String userId`                      | `List<UserTagDTO>`       | 获取用户标签     |
| PUT    | /tags                | `UpdateTagsRequestDTO`               | `Void`                   | 更新标签         |
| GET    | /settings/{userId}   | `String userId`                      | `UserSettingsDTO`        | 获取设置         |
| PUT    | /settings            | `UpdateSettingsDTO`                  | `Void`                   | 更新设置         |
| GET    | /stats/{userId}      | `String userId`                      | `UserStatsDTO`           | 获取用户统计     |

### 内容服务 `/content`

| 方法   | 路径                          | 请求参数                                  | 返回                      | 说明         |
| ------ | ----------------------------- | ----------------------------------------- | ------------------------- | ------------ |
| POST   | /posts                        | `CreatePostDTO`                           | `PostDTO`                 | 发布帖子     |
| GET    | /posts/{postId}               | `Long postId`                             | `PostDetailDTO`           | 帖子详情     |
| PUT    | /posts/{postId}               | `Long postId, UpdatePostDTO`              | `PostDTO`                 | 更新帖子     |
| DELETE | /posts/{postId}               | `Long postId`                             | `Void`                    | 删除帖子     |
| GET    | /posts/user/{userId}          | `String userId, PageRequestDTO page`      | `PageDTO<PostDTO>`        | 用户帖子列表 |
| GET    | /feed/following               | `PageRequestDTO page`                     | `PageDTO<PostDTO>`        | 关注Feed     |
| GET    | /feed/recommend               | `PageRequestDTO page`                     | `PageDTO<PostDTO>`        | 推荐Feed     |
| POST   | /posts/{postId}/comments      | `Long postId, CreateCommentDTO`           | `CommentDTO`              | 发表评论     |
| GET    | /posts/{postId}/comments      | `Long postId, CommentPageRequestDTO page` | `PageDTO<CommentDTO>`     | 评论列表     |
| GET    | /posts/{postId}/comments/tree | `Long postId, PageRequestDTO page`        | `PageDTO<CommentTreeDTO>` | 评论树       |
| POST   | /favorites/{postId}           | `Long postId`                             | `Void`                    | 收藏帖子     |
| DELETE | /favorites/{postId}           | `Long postId`                             | `Void`                    | 取消收藏     |
| GET    | /favorites                    | `PageRequestDTO page`                     | `PageDTO<PostDTO>`        | 收藏列表     |
| GET    | /tags                         | `PageRequestDTO page`                     | `PageDTO<TagDTO>`         | 标签列表     |
| GET    | /tags/{tagId}/posts           | `Long tagId, PageRequestDTO page`         | `PageDTO<PostDTO>`        | 标签下帖子   |

### 互动服务 `/interaction`

| 方法   | 路径                        | 请求参数                             | 返回                       | 说明             |
| ------ | --------------------------- | ------------------------------------ | -------------------------- | ---------------- |
| POST   | /like/{postId}              | `Long postId`                        | `LikeResultDTO`            | 点赞             |
| DELETE | /unlike/{postId}            | `Long postId`                        | `LikeResultDTO`            | 取消点赞         |
| POST   | /like/status                | `LikeStatusRequestDTO`               | `Map<Long, Boolean>`       | 批量查询点赞状态 |
| GET    | /posts/{postId}/likers      | `Long postId, PageRequestDTO page`   | `PageDTO<UserDTO>`         | 点赞用户列表     |
| GET    | /user/{userId}/likes        | `String userId, PageRequestDTO page` | `PageDTO<PostDTO>`         | 用户点赞的帖子   |
| GET    | /notifications/stream       | `String userId`                      | `SseEmitter`               | SSE连接          |
| GET    | /notifications              | `PageRequestDTO page`                | `PageDTO<NotificationDTO>` | 通知列表         |
| GET    | /notifications/unread/count | -                                    | `Integer`                  | 未读计数         |
| POST   | /notifications/read/{id}    | `String notificationId`              | `Void`                     | 标记已读         |
| POST   | /notifications/read/all     | -                                    | `Void`                     | 全部已读         |
| GET    | /inbox                      | `PageRequestDTO page`                | `PageDTO<InboxItemDTO>`    | 收件箱           |
| GET    | /inbox/unread               | -                                    | `List<InboxItemDTO>`       | 未读消息         |
| GET    | /history/likes              | `PageRequestDTO page`                | `PageDTO<InteractionDTO>`  | 点赞历史         |
| GET    | /history/comments           | `PageRequestDTO page`                | `PageDTO<InteractionDTO>`  | 评论历史         |

### 搜索服务 `/search`

| 方法 | 路径          | 请求参数                      | 返回                  | 说明     |
| ---- | ------------- | ----------------------------- | --------------------- | -------- |
| GET  | /posts        | `PostSearchRequestDTO`        | `PageDTO<PostDTO>`    | 搜索帖子 |
| GET  | /users        | `UserSearchRequestDTO`        | `PageDTO<UserDTO>`    | 搜索用户 |
| GET  | /comments     | `CommentSearchRequestDTO`     | `PageDTO<CommentDTO>` | 搜索评论 |
| POST | /advanced     | `AdvancedSearchDTO`           | `PageDTO<PostDTO>`    | 高级搜索 |
| GET  | /suggest      | `String prefix, Integer size` | `List<String>`        | 搜索建议 |
| GET  | /suggest/tags | `String prefix`               | `List<TagDTO>`        | 标签建议 |
| GET  | /hot/words    | `Integer limit`               | `List<HotWordDTO>`    | 热搜词   |
| GET  | /hot/posts    | `Integer limit`               | `List<PostDTO>`       | 热搜帖子 |

### 文件服务 `/files`

| 方法   | 路径                   | 请求参数                                            | 返回               | 说明       |
| ------ | ---------------------- | --------------------------------------------------- | ------------------ | ---------- |
| POST   | /upload                | `MultipartFile file, String type`                   | `FileDTO`          | 上传文件   |
| POST   | /uploads               | `MultipartFile[] files`                             | `List<FileDTO>`    | 批量上传   |
| POST   | /upload/image          | `MultipartFile file, ImageUploadOptionsDTO options` | `ImageDTO`         | 上传图片   |
| POST   | /upload/avatar         | `MultipartFile file`                                | `ImageDTO`         | 上传头像   |
| POST   | /upload/chunk/init     | `InitChunkUploadDTO`                                | `ChunkUploadDTO`   | 初始化分片 |
| POST   | /upload/chunk          | `ChunkUploadDTO chunk`                              | `Void`             | 上传分片   |
| POST   | /upload/chunk/complete | `CompleteChunkDTO`                                  | `FileDTO`          | 完成分片   |
| GET    | /download/{fileId}     | `String fileId`                                     | `Resource`         | 下载文件   |
| GET    | /preview/{fileId}      | `String fileId, Integer width, Integer height`      | `Resource`         | 预览图片   |
| GET    | /info/{fileId}         | `String fileId`                                     | `FileInfoDTO`      | 文件信息   |
| DELETE | /delete/{fileId}       | `String fileId`                                     | `Void`             | 删除文件   |
| GET    | /list                  | `FileListRequestDTO`                                | `PageDTO<FileDTO>` | 文件列表   |

### 推荐服务 `/recommend`

| 方法 | 路径                    | 请求参数                       | 返回                  | 说明       |
| ---- | ----------------------- | ------------------------------ | --------------------- | ---------- |
| GET  | /feed                   | `RecommendRequestDTO`          | `PageDTO<PostDTO>`    | 推荐Feed   |
| GET  | /feed/explain/{postId}  | `Long postId`                  | `RecommendExplainDTO` | 推荐解释   |
| GET  | /users/follow           | `Integer limit`                | `List<UserDTO>`       | 推荐关注   |
| GET  | /users/similar/{userId} | `String userId, Integer limit` | `List<UserDTO>`       | 相似用户   |
| GET  | /posts/similar/{postId} | `Long postId, Integer limit`   | `List<PostDTO>`       | 相似帖子   |
| GET  | /tags/recommend         | `Integer limit`                | `List<TagDTO>`        | 推荐标签   |
| POST | /feedback/like          | `FeedbackDTO`                  | `Void`                | 喜欢反馈   |
| POST | /feedback/dislike       | `FeedbackDTO`                  | `Void`                | 不喜欢反馈 |
