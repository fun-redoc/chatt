package com.rsh.probe.chat;

import com.rsh.probe.chat.model.Post;
import com.rsh.probe.chat.model.User;
import com.rsh.probe.chat.security.SecurityConfiguration;
import com.rsh.probe.chat.service.PostService;
import com.rsh.probe.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@SpringBootApplication
public class ChatApplication {
	// state managemnt bit field
	private static final byte state_start        = 0b0000_0000;
	private static final byte state_passwd       = 0b0000_0001;
	private static final byte state_user         = 0b0000_0010;
	private static final byte state_text         = 0b0000_0100;
	private static final byte state_user_ready   = 0b0000_0011;
	private static final byte state_all_ready    = 0b0000_0111;


	/**
	 * helper method to store objects into db on startf
	 * TODO only for testing purposes
	 * @param ctx
	 * @param username
	 * @param passwd
	 * @param entry
	 */
	private static void testDataForUser(ConfigurableApplicationContext ctx,  String username, String passwd, String entry) {
		final UserService userService = ctx.getBean(UserService.class);
		final PostService postService = ctx.getBean(PostService.class);
		var savedUser = userService.save(new User(username, passwd));
		var post = new Post();
		post.setUser(savedUser);
		post.setText(entry);
		postService.save(post);
	}

	/**
	 * you can pass some testdata to be stored on application start
	 * command line parameter:
	 * -test_user=<user name>
	 * -test_passwd=<users password>
	 * -test_entry=<post message of the last user>
	 * you can use those arguments multiple times to create multiple test user and postings
	 * e.g.
	 *     -test_user=me -test_passwd=secrete -test_entry="Hello World." -test_user=you -test_entry="Hello Universe."
	 *     creates 2 test users (me and you), both will have the same secrete password
	 *     me has the post: Hello World.
	 *     you have the post: Hello Universe.
	 * @param args
	 */
	public static void main(String[] args) {
		final var ctx = SpringApplication.run(ChatApplication.class, args);
		if(args.length > 0) {
			var testArgs = Arrays.stream(args).filter(arg -> arg.startsWith("test")).toList();
			byte state = 0;
			String user_name = null;
			String user_passwd = null;
			String text = null;
			for(var arg : testArgs) {
				var option = arg.split("=");
				if(option.length == 2) {
					var option_name = option[0];
					var option_val = option[1];
					switch (option_name) {
						case "test_user":
							user_name = option_val;
							state |= state_user;
							break;
						case "test_passwd":
							user_passwd = option_val;
							state |= state_passwd;
							break;
						case "test_entry":
							text = option_val;
							state |= state_text;
							break;
					}
					if ((state & state_all_ready) == state_all_ready) {
						testDataForUser(ctx, user_name, user_passwd, text);
					}
				}
			}
		}
	}

}
