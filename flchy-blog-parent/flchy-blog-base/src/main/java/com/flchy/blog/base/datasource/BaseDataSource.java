package com.flchy.blog.base.datasource;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.flchy.blog.base.datasource.annotation.BaseRepository;

/**
 * @Primary 标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean
 *          优先被考虑。「多数据源配置的时候注意，必须要有一个主数据源，用 @Primary 标志该 Bean」
 * @MapperScan 扫描 Mapper 接口并容器管理，包路径精确到 base，为了和下面 ods 数据源做到精确区分
 * @Value 获取全局配置文件 application.properties 的 kv 配置,并自动装配 sqlSessionFactoryRef
 *        表示定义了 key ，表示一个唯一 SqlSessionFactory 实例
 */
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = BaseDataSource.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory", annotationClass = BaseRepository.class)
public class BaseDataSource {

	static final String PACKAGE = "com.flchy.**.dao";
	static final String MAPPER_LOCATION = "classpath*:conf/mapper/**/*.xml";
	// 连接Url路径
	@Value("${base.datasource.url}")
	private String url;
	// 用户名 
	@Value("${base.datasource.username}")
	private String user;
	// 密码
	@Value("${base.datasource.password}")
	private String password;
	// 驱动类路径
	@Value("${base.datasource.driverClassName}")
	private String driverClass;

	// 常用的插件有： 监控统计用的filter:stat,日志用的filter:log4j,防御sql注入的filter:wall
	@Value("${base.datasource.druid.filters}")
	private String filters;

	// 初始化连接数量
	@Value("${base.datasource.druid.initialSize}")
	private int initialSize;

	// 最大连接池数量
	@Value("${base.datasource.druid.maxActive}")
	private int maxActive;

	// 配置获取连接等待超时的时间
	@Value("${base.datasource.druid.maxWait}")
	private int maxWait;

	// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
	@Value("${base.datasource.druid.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	// 配置一个连接在池中最小生存的时间，单位是毫秒
	@Value("${base.datasource.druid.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	// 用来检测连接是否有效的sql，要求是一个查询语句
	@Value("${base.datasource.druid.validationQuery}")
	private String validationQuery;

	// 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于 timeBetweenEvictionRunsMillis，
	// 执行validationQuery检测连接是否有效。
	@Value("${base.datasource.druid.testWhileIdle}")
	private boolean testWhileIdle;

	// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
	@Value("${base.datasource.druid.testOnReturn}")
	private boolean testOnBorrow;

	// 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
	@Value("${base.datasource.druid.testOnReturn}")
	private boolean testOnReturn;

	// 是否缓存preparedStatement，也就是PSCache。 PSCache对支持游标的数据库性能提升巨大，比如说oracle。
	// 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。作者在5.5版本中使用PSCache，通过监控界面发现PSCache有缓存命中率记录，该应该是支持PSCache。
	@Value("${base.datasource.druid.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	// 指定每个连接上preparedStatement的大小
	@Value("${base.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
	private int maxPoolPreparedStatementPerConnectionSize;

	@Bean(name = "masterDataSource")
	@Primary
	public DataSource masterDataSource() throws SQLException {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(password);

		dataSource.setFilters(filters);
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxActive(maxActive);
		dataSource.setMaxWait(maxWait);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		dataSource.setValidationQuery(validationQuery);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTestOnBorrow(testOnBorrow);
		dataSource.setTestOnReturn(testOnReturn);
		dataSource.setPoolPreparedStatements(poolPreparedStatements);
		dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
		return dataSource;
	}

	@Bean(name = "masterTransactionManager")
	@Primary
	public DataSourceTransactionManager masterTransactionManager() {
		try {
			return new DataSourceTransactionManager(masterDataSource());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    @Bean
    public GlobalConfiguration globalConfig(){
        GlobalConfiguration globalConfiguration=new GlobalConfiguration();
//          <!-- 主键策略配置 -->
//        可选参数
//        AUTO->`0`("数据库ID自增")
//        INPUT->`1`(用户输入ID")
//        ID_WORKER->`2`("全局唯一ID")
//        UUID->`3`("全局唯一ID")

        globalConfiguration.setIdType(0);

//    <!-- 数据库类型配置 -->
//    <!-- 可选参数（默认mysql）
//        MYSQL->`mysql`
//        ORACLE->`oracle`
//        DB2->`db2`
//        H2->`h2`
//        HSQL->`hsql`
//        SQLITE->`sqlite`
//        POSTGRE->`postgresql`
//        SQLSERVER2005->`sqlserver2005`
//        SQLSERVER->`sqlserver`
//        -->
//    <property name="dbType" value="oracle"/>
        globalConfiguration.setDbType("mysql");

        return globalConfiguration;
    }

	@Bean(name = "masterSqlSessionFactory")
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
			throws Exception {
		final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
		sessionFactory.setDataSource(masterDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(BaseDataSource.MAPPER_LOCATION));

		Properties p = new Properties();
		p.setProperty("cacheEnabled", "true");
		// <!-- 查询时，关闭关联对象即时加载以提高性能 -->
		p.setProperty("lazyLoadingEnabled", "false");
		// <!-- 设置关联对象加载的形态，此处为按需加载字段(加载字段由SQL指定)，不会加载关联表的所有字段，以提高性能 -->
		p.setProperty("aggressiveLazyLoading", "true");
		// <!-- 对于未知的SQL查询，允许返回不同的结果集以达到通用的效果 -->
		p.setProperty("multipleResultSetsEnabled", "true");
		// <!-- 允许使用列标签代替列名 -->
		p.setProperty("useColumnLabel", "true");
		// <!-- 允许使用自定义的主键值(比如由程序生成的UUID 32位编码作为键值)，数据表的PK生成策略将被覆盖 -->
		p.setProperty("useGeneratedKeys", "true");
		// <!-- 给予被嵌套的resultMap以字段-属性的映射支持 -->
		p.setProperty("autoMappingBehavior", "FULL");
		// <!-- 对于批量更新操作缓存SQL以提高性能 -->
		p.setProperty("defaultExecutorType", "BATCH");
		// <!-- 数据库超过25000秒仍未响应则超时 -->
		p.setProperty("defaultStatementTimeout", "25000");

		PaginationInterceptor paginationInterceptor=new PaginationInterceptor();
		paginationInterceptor.setDialectType("mysql");
		sessionFactory.setConfigurationProperties(p);
	        Interceptor[] interceptors=new Interceptor[3];
	        Interceptor interceptor=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor.plugin(paginationInterceptor);
	        interceptors[0]=interceptor;
	        
	        // <!-- SQL 执行分析拦截器 stopProceed 发现全表执行 delete update 是否停止运行 -->
	        SqlExplainInterceptor sqlExplainInterceptor=new SqlExplainInterceptor();
	        sqlExplainInterceptor.setStopProceed(true);
	        Interceptor interceptor1=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor1.plugin(sqlExplainInterceptor);
	        interceptors[1]=interceptor1;
	        
	     // <!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->
	        PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
	        //执行最大时长，超过自动停止运行，有助于发现问题。
	        performanceInterceptor.setMaxTime(100);
	        //SQL是否格式化 默认false
	        performanceInterceptor.setFormat(true);
	        Interceptor interceptor2=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor2.plugin(performanceInterceptor);
	        interceptors[2]=interceptor2;
	        
	        sessionFactory.setPlugins(interceptors);
	        sessionFactory.setGlobalConfig(globalConfig());
		
		return sessionFactory.getObject();
	}

}
