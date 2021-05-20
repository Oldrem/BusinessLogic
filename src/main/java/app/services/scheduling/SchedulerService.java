package app.services.scheduling;

import app.repositories.OrderRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

@Service("schedulerService")
public class SchedulerService
{
    private OrderRepository orders;
    private Scheduler scheduler;

    public SchedulerService(OrderRepository orders) throws SchedulerException
    {
        this.orders = orders;

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.getContext().put("orders", orders);
        scheduler.start();
        scheduleOrderCancellation();
    }

    private void scheduleOrderCancellation() throws SchedulerException
    {
        JobDetail job = JobBuilder.newJob(UnpaidOrderCancellationJob.class)
                .withIdentity("unpaidOrderCancellation", "app")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("unpaidOrderCancellationTrigger", "app")
                .startNow()
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds((int) UnpaidOrderCancellationJob.checkPeriod.getSeconds())
                        .repeatForever())
                .build();
        scheduler.scheduleJob(job, trigger);
    }
}
