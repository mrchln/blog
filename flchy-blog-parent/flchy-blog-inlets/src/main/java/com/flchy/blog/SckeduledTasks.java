package com.flchy.blog;

import java.util.Collection;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.common.service.ScheduledService;

/**
 * 定时任务
 * @author flchy
 *
 */
@Component
public class SckeduledTasks {
	private Collection<ScheduledService> scheduledServices;
	/**
	 * 表示每隔10000ms
	 * @throws InterruptedException
	 */
	@Scheduled(fixedRate = 1000000)
	public void loadProperties() throws InterruptedException {
		try {
			PropertiesHolder.loadProperties();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 表示当方法执行完毕10分钟后，Spring scheduling会再次调用该方法
	 * @throws InterruptedException 异常
	 */
	@Scheduled(fixedDelay = 60*1000*10)//60*1000*10
	public void reportCurrentTimeAfterSleep() throws InterruptedException {
		this.scheduledServices = SpringContextHolder.getBeansOfType(ScheduledService.class).values();
		if (this.scheduledServices == null) {
			return;
		}
		for (ScheduledService scheduledService : scheduledServices) {
			scheduledService.schedule();
		}
	}

	/**
	 * 提供了一种通用的定时任务表达式，这里表示每隔5秒执行一次，更加详细的信息可以参考cron表达式。
	 * @throws InterruptedException
	 */
	@Scheduled(cron = "0 0 1 * * *")
	public void reportCurrentTimeCron() throws InterruptedException {
	}
}
