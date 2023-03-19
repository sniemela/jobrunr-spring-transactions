# JobRunr with Spring transactions

This is an example Spring boot application that shows how to 
make JobRunr to honor and participate in Spring managed transactions. By default, JobRunr community edition 
is not aware of Spring transactions (JobRunr Pro v5+ is). JobRunr CE creates its own transaction and commits it when jobs are enqueued or scheduled.
This can be a problem when one wants to schedule multiple jobs in one transaction and expect all jobs to be rollbacked when 
something goes wrong in the middle of a Spring managed transaction.

This example project uses a customised version of Spring's [TransactionAwareDataProxy](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/TransactionAwareDataSourceProxy.html) 
which doesn't do anything on `DataSource.commit()` if a Spring managed transaction is active.
This proxy is used by JobRunr only and it's handed over to JobRurn by providing a custom `StorageProvider` implementation.

