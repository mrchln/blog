package com.flchy.blog.base.datasource;

import java.sql.SQLException;

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
import com.baomidou.mybatisplus.MybatisConfiguration;
import com.baomidou.mybatisplus.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.entity.GlobalConfiguration;
import com.baomidou.mybatisplus.enums.DBType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.baomidou.mybatisplus.spring.boot.starter.SpringBootVFS;
import com.flchy.blog.base.datasource.annotation.ConfigRepository;

/**
 * @Primary 标志这个 Bean 如果在多个同类 Bean 候选时，该 Bean 优先被考虑。「多数据源配置的时候注意，必须要有一个主数据源，用 @Primary 标志该 Bean」
 * @MapperScan 扫描 Mapper 接口并容器管理，包路径精确到 base，为了和下面 ods 数据源做到精确区分
 * @Value 获取全局配置文件 application.properties 的 kv 配置,并自动装配 sqlSessionFactoryRef 表示定义了 key ，表示一个唯一 SqlSessionFactory 实例
 */
@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = ConfigDataSource.PACKAGE, sqlSessionFactoryRef = "clusterSqlSessionFactory", annotationClass = ConfigRepository.class)
public class ConfigDataSource {
	static final String PACKAGE = "com.flchy.blog.**.mapper";
	static final String MAPPER_LOCATION = "classpath*:conf/mapper/**/*.xml";
	// 连接Url路径
		@Value("${configs.datasource.url}")
		private String url;
		// 用户名
		@Value("${configs.datasource.username}")
		private String user;
		// 密码
		@Value("${configs.datasource.password}")
		private String password;
		// 驱动类路径
		@Value("${configs.datasource.driverClassName}")
		private String driverClass;

		// 常用的插件有： 监控统计用的filter:stat,日志用的filter:log4j,防御sql注入的filter:wall
		@Value("${configs.datasource.druid.filters}")
		private String filters;

		// 初始化连接数量
		@Value("${configs.datasource.druid.initialSize}")
		private int initialSize;

		// 最大连接池数量
		@Value("${configs.datasource.druid.maxActive}")
		private int maxActive;

		// 配置获取连接等待超时的时间
		@Value("${configs.datasource.druid.maxWait}")
		private int maxWait;

		// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
		@Value("${configs.datasource.druid.timeBetweenEvictionRunsMillis}")
		private int timeBetweenEvictionRunsMillis;

		// 配置一个连接在池中最小生存的时间，单位是毫秒
		@Value("${configs.datasource.druid.minEvictableIdleTimeMillis}")
		private int minEvictableIdleTimeMillis;

		// 用来检测连接是否有效的sql，要求是一个查询语句
		@Value("${configs.datasource.druid.validationQuery}")
		private String validationQuery;

		// 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于 timeBetweenEvictionRunsMillis， 执行validationQuery检测连接是否有效。
		@Value("${configs.datasource.druid.testWhileIdle}")
		private boolean testWhileIdle;

		// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。
		@Value("${configs.datasource.druid.testOnReturn}")
		private boolean testOnBorrow;

		// 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
		@Value("${configs.datasource.druid.testOnReturn}")
		private boolean testOnReturn;

		// 是否缓存preparedStatement，也就是PSCache。 PSCache对支持游标的数据库性能提升巨大，比如说oracle。 在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。作者在5.5版本中使用PSCache，通过监控界面发现PSCache有缓存命中率记录，该应该是支持PSCache。
		@Value("${configs.datasource.druid.poolPreparedStatements}")
		private boolean poolPreparedStatements;

		// 指定每个连接上preparedStatement的大小
		@Value("${configs.datasource.druid.maxPoolPreparedStatementPerConnectionSize}")
		private int maxPoolPreparedStatementPerConnectionSize;

	@Bean(name = "clusterDataSource")
	public DataSource clusterDataSource() throws SQLException {
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

	@Bean(name = "clusterTransactionManager")
	public DataSourceTransactionManager clusterTransactionManager() {
		try {
			return new DataSourceTransactionManager(clusterDataSource());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	/**
	 * 
	 * mybatis-plus分页
	 * 
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor page = new PaginationInterceptor();
		page.setDialectType("mysql");
		return page;
	}

	@Bean(name = "clusterSqlSessionFactory")
	public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("clusterDataSource") DataSource clusterDataSource)
			throws Exception {
		final MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
		sessionFactory.setDataSource(clusterDataSource);
		sessionFactory.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources(ConfigDataSource.MAPPER_LOCATION));
		sessionFactory.setVfs(SpringBootVFS.class);

		GlobalConfiguration globalConfig = new GlobalConfiguration();
		globalConfig.setDbType(DBType.MYSQL.name());
		globalConfig.setIdType(2);
		sessionFactory.setGlobalConfig(globalConfig);
		SqlExplainInterceptor sqlExplainInterceptor = new SqlExplainInterceptor();
		sqlExplainInterceptor.setStopProceed(true);
		sessionFactory.setPlugins(new Interceptor[] { paginationInterceptor(), sqlExplainInterceptor });
		MybatisConfiguration mc = new MybatisConfiguration();
		mc.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		sessionFactory.setConfiguration(mc);
		return sessionFactory.getObject();
	}

}
