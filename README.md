## `SimpleForum`

### 项目介绍

- 这是一个简单的论坛系统的后端，为前端提供较为规范的`RESTful API`服务
- 功能涉及用户角色/权限管理，版块、帖子、评论的增删查
- 项目依赖：
  - 基于`Spring Boot 4.0.0`与`Spring MVC`框架搭建
  - 使用`MySQL`数据库
  - 使用`JPA`作为`ORM`并集成`Spring Data JPA`
  - 使用`Redis`作为缓存层并集成`Spring Data Redis`与`Spring Cache`
  - 使用`Bean Validation`作为前端数据验证并集成`Spring Validation`
  - 加密部分借用了`Spring Security`的`PasswordEncoder`接口(但不强依赖于它)
- [前端网页项目](https://github.com/ishi-o/SimpleForumClient)

### `API`介绍

- 包括以下`API`(`URL`上下文为`/api`，所有响应的数据类型均为`application/json`)：
  
  |请求方法|`URL`|请求数据类型(若有请求体)|可选`URL`参数|功能介绍|
  |--|--|--|--|--|
  |`GET`|`/auth/me`|-|-|获取会话的用户信息|
  |`POST`|`/auth/register`|`application/json`|-|注册用户|
  |`POST`|`/auth/login`|`application/json`|-|用户登录|
  |`POST`|`/auth/logout`|-|-|用户登出|
  |`POST`|`/auth/guest`|-|-|获取暂时的游客会话|
  |`GET`|`/boards`|-|`page`：分页页码(默认`0`)<br>`size`：分页大小(默认`10`)<br>`q`：查询关键词|分页查询版块信息|
  |`GET`|`/boards/{bid}`|-|-|获取指定版块|
  |`POST`|`/boards`|`application/json`|-|创建版块|
  |`PUT`|`/boards/{bid}`|`application/json`|-|更新指定版块|
  |`PATCH`|`/boards/{bid}/post-pin/{pid}`|-|-|切换帖子的置顶状态|
  |`DELETE`|`/boards/{bid}`|-|-|删除指定版块|
  |`GET`|`/users/{uid}/boards`|-|`page`：分页页码(默认`0`)<br>`size`：分页大小(默认`10`)|获取用户管理的版块|
  |`GET`|`/boards/{bid}/posts`|-|`page`：分页页码(默认`0`)<br>`size`：分页大小(默认`10`)<br>`q`：查询关键词|分页查询指定版块的帖子|
  |`GET`|`/boards/{bid}/posts`|-|-|获取指定帖子|
  |`POST`|`/boards/{bid}/posts`|`application/json`|-|创建帖子|
  |`DELETE`|`/boards/{bid}/posts/{pid}`|-|-|删除指定帖子|
  |`GET`|`/boards/{bid}/posts/{pid}/comments`|-|`page`：分页页码(默认`0`)<br>`size`：分页大小(默认`10`)|获取指定帖子的所有评论|
  |`GET`|`/boards/{bid}/posts/{pid}/comments/{cid}`|-|`page`：分页页码(默认`0`)<br>`size`：分页大小(默认`10`)|获取指定评论的所有回复|
  |`POST`|`/boards/{bid}/posts/{pid}/comments`|`text/plain`|-|评论帖子|
  |`POST`|`/boards/{bid}/posts/{pid}/comments/{cid}`|`text/plain`|-|回复帖子的评论|
  |`POST`|`/boards/{bid}/posts/{pid}/comments/{cid}/replies/{targetId}`|`text/plain`|-|回复其它回复|

- 用户角色/权限管理：针对此方面，目前没有对权限进行细分，而只是用角色作为一个粗粒度的权限管理
  - 游客：只能获取版块、帖子信息，无法增删改以及无法和获取评论信息
  - 一般用户：能访问所有`GET`方法且无权限检查，允许访问增删改方法但只有具体实体的主人能成功访问
  - 管理员：能正常访问所有方法，目前没有服务接口能注册管理员用户
- 版块管理：支持搜索服务
- 帖子管理：支持置顶服务、搜索服务
