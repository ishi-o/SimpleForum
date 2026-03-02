package com.dlut.forumx.search.consumer;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import com.dlut.forumx.commons.dto.mq.PostSyncMessage;
import com.dlut.forumx.commons.dto.mq.UserSyncMessage;
import com.dlut.forumx.search.document.PostDocument;
import com.dlut.forumx.search.document.UserDocument;
import com.dlut.forumx.search.repository.PostRepository;
import com.dlut.forumx.search.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * HTML转纯文本工具
 */
class HtmlUtils {

	/**
	 * 将HTML转换为纯文本（用于ES搜索）
	 */
	public static String toPlainText(String html) {
		if (html == null || html.isEmpty()) {
			return "";
		}
		return Jsoup.parse(html).text();
	}
}

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchSyncConsumer {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	/**
	 * 帖子同步消费者
	 */
	@Slf4j
	@Component
	@RequiredArgsConstructor
	@RocketMQMessageListener(topic = "post-sync-topic", consumerGroup = "search-post-consumer", selectorExpression = "*")
	public static class PostSyncConsumer implements RocketMQListener<PostSyncMessage> {

		private final PostRepository postRepository;

		@Override
		public void onMessage(PostSyncMessage message) {
			log.info("收到帖子同步消息: postId={}, action={}", message.getPostId(), message.getAction());

			try {
				switch (message.getAction()) {
					case "DELETE":
						// 删除索引
						postRepository.deleteById(message.getPostId());
						log.info("删除帖子索引: {}", message.getPostId());
						break;

					case "CREATE":
					case "UPDATE":
						// 这里应该通过Feign调用content-service获取完整帖子信息
						// 暂时模拟数据
						PostDocument postDoc = new PostDocument();
						postDoc.setId(message.getPostId());
						postDoc.setUserId(1L);
						postDoc.setUsername("testuser");
						postDoc.setNickname("测试用户");
						postDoc.setTitle("测试帖子标题");

						// 重要：HTML转纯文本
						String htmlContent = "<p>这是一段内容<img src='xxx.jpg'/>包含图片</p>";
						String plainText = HtmlUtils.toPlainText(htmlContent);
						postDoc.setContent(plainText); // 存纯文本 "这是一段内容包含图片"

						postDoc.setTags(List.of("测试", "帖子"));
						postDoc.setCreateTime(LocalDateTime.now());

						postRepository.save(postDoc);
						log.info("索引帖子成功: {}", message.getPostId());
						break;
				}
			} catch (Exception e) {
				log.error("处理帖子同步消息失败", e);
			}
		}
	}

	/**
	 * 用户同步消费者
	 */
	@Slf4j
	@Component
	@RequiredArgsConstructor
	@RocketMQMessageListener(topic = "user-sync-topic", consumerGroup = "search-user-consumer", selectorExpression = "*")
	public static class UserSyncConsumer implements RocketMQListener<UserSyncMessage> {

		private final UserRepository userRepository;

		@Override
		public void onMessage(UserSyncMessage message) {
			log.info("收到用户同步消息: userId={}, action={}", message.getUserId(), message.getAction());

			try {
				switch (message.getAction()) {
					case "DELETE":
						userRepository.deleteById(message.getUserId());
						log.info("删除用户索引: {}", message.getUserId());
						break;

					case "CREATE":
					case "UPDATE":
						// 这里应该通过Feign调用user-service获取完整用户信息
						// 暂时模拟数据
						UserDocument userDoc = new UserDocument();
						userDoc.setId(message.getUserId());
						userDoc.setUsername("testuser");
						userDoc.setNickname("测试用户");
						userDoc.setBio("这是一个测试用户的简介");
						userDoc.setRegisterTime(LocalDateTime.now());

						userRepository.save(userDoc);
						log.info("索引用户成功: {}", message.getUserId());
						break;
				}
			} catch (Exception e) {
				log.error("处理用户同步消息失败", e);
			}
		}
	}
}
