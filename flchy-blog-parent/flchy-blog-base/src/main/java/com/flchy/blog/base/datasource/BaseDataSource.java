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
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import com.flchy.blog.base.datasource.annotation.BaseRepository;

/**
 * @Primary 鏍囧織杩欎釜 Bean 濡傛灉鍦ㄥ涓悓绫� Bean 鍊欓�夋椂锛岃 Bean
 *          浼樺厛琚�冭檻銆傘�屽鏁版嵁婧愰厤缃殑鏃跺�欐敞鎰忥紝蹇呴』瑕佹湁涓�涓富鏁版嵁婧愶紝鐢� @Primary 鏍囧織璇� Bean銆�
 * @MapperScan 鎵弿 Mapper 鎺ュ彛骞跺鍣ㄧ鐞嗭紝鍖呰矾寰勭簿纭埌 base锛屼负浜嗗拰涓嬮潰 ods 鏁版嵁婧愬仛鍒扮簿纭尯鍒�
 * @Value 鑾峰彇鍏ㄥ眬閰嶇疆鏂囦欢 application.properties 鐨� kv 閰嶇疆,骞惰嚜鍔ㄨ閰� sqlSessionFactoryRef
 *        琛ㄧず瀹氫箟浜� key 锛岃〃绀轰竴涓敮涓� SqlSessionFactory 瀹炰緥
 */
@Configuration
// 鎵弿 Mapper 鎺ュ彛骞跺鍣ㄧ鐞�
@MapperScan(basePackages = BaseDataSource.PACKAGE, sqlSessionFactoryRef = "masterSqlSessionFactory", annotationClass = BaseRepository.class)
public class BaseDataSource {

	static final String PACKAGE = "com.flchy.**.dao";
	static final String MAPPER_LOCATION = "classpath*:conf/mapper/**/*.xml";
	// 杩炴帴Url璺緞
	@Value("${base.datasource.url}")
	private String url;
	// 鐢ㄦ埛鍚� 
	@Value("${base.datasource.username}")
	private String user;
	// 瀵嗙爜
	@Value("${base.datasource.password}")
	private String password;
	// 椹卞姩绫昏矾寰�
	@Value("${base.datasource.driverClassName}")
	private String driverClass;

	// 甯哥敤鐨勬彃浠舵湁锛� 鐩戞帶缁熻鐢ㄧ殑filter:stat,鏃ュ織鐢ㄧ殑filter:log4j,闃插尽sql娉ㄥ叆鐨刦ilter:wall
	@Value("${base.datasource.druid.filters}")
	private String filters;

	// 鍒濆鍖栬繛鎺ユ暟閲�
	@Value("${base.datasource.druid.initialSize}")
	private int initialSize;

	// 鏈�澶ц繛鎺ユ睜鏁伴噺
	@Value("${base.datasource.druid.maxActive}")
	private int maxActive;

	// 閰嶇疆鑾峰彇杩炴帴绛夊緟瓒呮椂鐨勬椂闂�
	@Value("${base.datasource.druid.maxWait}")
	private int maxWait;

	// 閰嶇疆闂撮殧澶氫箙鎵嶈繘琛屼竴娆℃娴嬶紝妫�娴嬮渶瑕佸叧闂殑绌洪棽杩炴帴锛屽崟浣嶆槸姣
	@Value("${base.datasource.druid.timeBetweenEvictionRunsMillis}")
	private int timeBetweenEvictionRunsMillis;

	// 閰嶇疆涓�涓繛鎺ュ湪姹犱腑鏈�灏忕敓瀛樼殑鏃堕棿锛屽崟浣嶆槸姣
	@Value("${base.datasource.druid.minEvictableIdleTimeMillis}")
	private int minEvictableIdleTimeMillis;

	// 鐢ㄦ潵妫�娴嬭繛鎺ユ槸鍚︽湁鏁堢殑sql锛岃姹傛槸涓�涓煡璇㈣鍙�
	@Value("${base.datasource.druid.validationQuery}")
	private String validationQuery;

	// 寤鸿閰嶇疆涓簍rue锛屼笉褰卞搷鎬ц兘锛屽苟涓斾繚璇佸畨鍏ㄦ�с�傜敵璇疯繛鎺ョ殑鏃跺�欐娴嬶紝濡傛灉绌洪棽鏃堕棿澶т簬 timeBetweenEvictionRunsMillis锛�
	// 鎵цvalidationQuery妫�娴嬭繛鎺ユ槸鍚︽湁鏁堛��
	@Value("${base.datasource.druid.testWhileIdle}")
	private boolean testWhileIdle;

	// 鐢宠杩炴帴鏃舵墽琛寁alidationQuery妫�娴嬭繛鎺ユ槸鍚︽湁鏁堬紝鍋氫簡杩欎釜閰嶇疆浼氶檷浣庢�ц兘銆�
	@Value("${base.datasource.druid.testOnReturn}")
	private boolean testOnBorrow;

	// 褰掕繕杩炴帴鏃舵墽琛寁alidationQuery妫�娴嬭繛鎺ユ槸鍚︽湁鏁堬紝鍋氫簡杩欎釜閰嶇疆浼氶檷浣庢�ц兘
	@Value("${base.datasource.druid.testOnReturn}")
	private boolean testOnReturn;

	// 鏄惁缂撳瓨preparedStatement锛屼篃灏辨槸PSCache銆� PSCache瀵规敮鎸佹父鏍囩殑鏁版嵁搴撴�ц兘鎻愬崌宸ㄥぇ锛屾瘮濡傝oracle銆�
	// 鍦╩ysql5.5浠ヤ笅鐨勭増鏈腑娌℃湁PSCache鍔熻兘锛屽缓璁叧闂帀銆備綔鑰呭湪5.5鐗堟湰涓娇鐢≒SCache锛岄�氳繃鐩戞帶鐣岄潰鍙戠幇PSCache鏈夌紦瀛樺懡涓巼璁板綍锛岃搴旇鏄敮鎸丳SCache銆�
	@Value("${base.datasource.druid.poolPreparedStatements}")
	private boolean poolPreparedStatements;

	// 鎸囧畾姣忎釜杩炴帴涓妏reparedStatement鐨勫ぇ灏�
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
//          <!-- 涓婚敭绛栫暐閰嶇疆 -->
//        鍙�夊弬鏁�
//        AUTO->`0`("鏁版嵁搴揑D鑷")
//        INPUT->`1`(鐢ㄦ埛杈撳叆ID")
//        ID_WORKER->`2`("鍏ㄥ眬鍞竴ID")
//        UUID->`3`("鍏ㄥ眬鍞竴ID")

        globalConfiguration.setIdType(0);

//    <!-- 鏁版嵁搴撶被鍨嬮厤缃� -->
//    <!-- 鍙�夊弬鏁帮紙榛樿mysql锛�
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
        //璋冭瘯浣跨敤
        globalConfiguration.setRefresh(true);

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
		// <!-- 鏌ヨ鏃讹紝鍏抽棴鍏宠仈瀵硅薄鍗虫椂鍔犺浇浠ユ彁楂樻�ц兘 -->
		p.setProperty("lazyLoadingEnabled", "false");
		// <!-- 璁剧疆鍏宠仈瀵硅薄鍔犺浇鐨勫舰鎬侊紝姝ゅ涓烘寜闇�鍔犺浇瀛楁(鍔犺浇瀛楁鐢盨QL鎸囧畾)锛屼笉浼氬姞杞藉叧鑱旇〃鐨勬墍鏈夊瓧娈碉紝浠ユ彁楂樻�ц兘 -->
		p.setProperty("aggressiveLazyLoading", "true");
		// <!-- 瀵逛簬鏈煡鐨凷QL鏌ヨ锛屽厑璁歌繑鍥炰笉鍚岀殑缁撴灉闆嗕互杈惧埌閫氱敤鐨勬晥鏋� -->
		p.setProperty("multipleResultSetsEnabled", "true");
		// <!-- 鍏佽浣跨敤鍒楁爣绛句唬鏇垮垪鍚� -->
		p.setProperty("useColumnLabel", "true");
		// <!-- 鍏佽浣跨敤鑷畾涔夌殑涓婚敭鍊�(姣斿鐢辩▼搴忕敓鎴愮殑UUID 32浣嶇紪鐮佷綔涓洪敭鍊�)锛屾暟鎹〃鐨凱K鐢熸垚绛栫暐灏嗚瑕嗙洊 -->
		p.setProperty("useGeneratedKeys", "true");
		// <!-- 缁欎簣琚祵濂楃殑resultMap浠ュ瓧娈�-灞炴�х殑鏄犲皠鏀寔 -->
		p.setProperty("autoMappingBehavior", "FULL");
		// <!-- 瀵逛簬鎵归噺鏇存柊鎿嶄綔缂撳瓨SQL浠ユ彁楂樻�ц兘 -->
		p.setProperty("defaultExecutorType", "BATCH");
		// <!-- 鏁版嵁搴撹秴杩�25000绉掍粛鏈搷搴斿垯瓒呮椂 -->
		p.setProperty("defaultStatementTimeout", "25000");
		p.setProperty("logImpl", "STDOUT_LOGGING");
		sessionFactory.setConfigurationProperties(p);
		//閰嶇疆鐨勫垎椤�
		PaginationInterceptor paginationInterceptor=new PaginationInterceptor();
		paginationInterceptor.setDialectType(DbType.MYSQL.getValue());
	        Interceptor[] interceptors=new Interceptor[3];
	        Interceptor interceptor=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor.plugin(paginationInterceptor);
	        interceptors[0]=interceptor;
	        // <!-- SQL 鎵ц鍒嗘瀽鎷︽埅鍣� stopProceed 鍙戠幇鍏ㄨ〃鎵ц delete update 鏄惁鍋滄杩愯 -->
	        SqlExplainInterceptor sqlExplainInterceptor=new SqlExplainInterceptor();
	        sqlExplainInterceptor.setStopProceed(true);
	        Interceptor interceptor1=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor1.plugin(sqlExplainInterceptor);
	        interceptors[1]=interceptor1;
	        
	     // <!-- SQL 鎵ц鎬ц兘鍒嗘瀽锛屽紑鍙戠幆澧冧娇鐢紝绾夸笂涓嶆帹鑽愩�� maxTime 鎸囩殑鏄� sql 鏈�澶ф墽琛屾椂闀� -->
	        PerformanceInterceptor performanceInterceptor=new PerformanceInterceptor();
	        //鎵ц鏈�澶ф椂闀匡紝瓒呰繃鑷姩鍋滄杩愯锛屾湁鍔╀簬鍙戠幇闂銆�
	        performanceInterceptor.setMaxTime(100);
	        //SQL鏄惁鏍煎紡鍖� 榛樿false
	        performanceInterceptor.setFormat(true);
	        Interceptor interceptor2=new com.baomidou.mybatisplus.plugins.PaginationInterceptor();
	        interceptor2.plugin(performanceInterceptor);
	        interceptors[2]=interceptor2;
	        
	        sessionFactory.setPlugins(interceptors);
	        sessionFactory.setGlobalConfig(globalConfig());
		
		return sessionFactory.getObject();
	}

}
