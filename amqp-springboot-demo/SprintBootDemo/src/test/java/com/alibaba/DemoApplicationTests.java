package com.alibaba;

import com.alibaba.rabbit.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	private Sender sender;

	@Test
	public void hello() throws Exception {
		while (true) {
			try {
				sender.send();
				Thread.sleep(1000);
			} catch (Exception e) {
				;
			}
		}
	}
}
