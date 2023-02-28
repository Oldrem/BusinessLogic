package app.services.scheduling;

import app.repositories.OrderRepository;
import app.repositories.ProductRepository;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

@Service("schedulerService")
public class SchedulerService
{
    private OrderRepository orders;
    private ProductRepository products;
    private Scheduler scheduler;

    public SchedulerService(OrderRepository orders, ProductRepository products) throws SchedulerException
    {
        this.orders = orders;
        this.products = products;

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.getContext().put("orders", orders);
        scheduler.getContext().put("products", products);
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
