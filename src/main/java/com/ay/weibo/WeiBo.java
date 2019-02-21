package com.ay.weibo;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 发布微博
 * 互粉
 * 取关
 * 查看微博
 * @author Z
 *
 */
public class WeiBo {
	
	//HBase的配置对象
	private Configuration conf = HBaseConfiguration.create();
	
	//创建weibo这个业务的命名空间，3张表
	private static final byte[] NS_WEIBO = Bytes.toBytes("ns_weibo");
	private static final byte[] TABLE_CONTENT = Bytes.toBytes("ns_weibo:content");
	private static final byte[] TABLE_RELATION = Bytes.toBytes("ns_weibo:relation");
	private static final byte[] TABLE_INBOX = Bytes.toBytes("ns_weibo:inbox");
	
	private void init() throws IOException{
		//创建微博业务命名空间
		initNamespace();
		//创建微博内容表
		initTableContent();
		//创建用户关系表
		initTableRelation();
		//创建收件箱表
		initTableInbox();
	}
	
	private void initNamespace() throws IOException {
		// TODO Auto-generated method stub
		Connection connection = ConnectionFactory.createConnection(conf);
		Admin admin = connection.getAdmin();
		NamespaceDescriptor ns_weibo = NamespaceDescriptor
				.create("ns_weibo")
				.addConfiguration("creator", "ay")
				.addConfiguration("create_time", String.valueOf(System.currentTimeMillis()))
				.build();
		admin.createNamespace(ns_weibo);
		admin.close();
		connection.close();

	}
	/**
	 * 表名：ns_weibo:content
	 * 列族名：info
	 * 列名：content
	 * rowkey:用户id_时间戳
	 * value:微博内容（文字内容，图片URL，视频URL，语音URL）
	 * versions:1
	 * @throws IOException
	 */
	private void initTableContent() throws IOException {
		
		Connection connection = ConnectionFactory.createConnection(conf);
		Admin admin = connection.getAdmin();
		//创建表描述器
		HTableDescriptor contentTableDescriptor = new HTableDescriptor(TableName.valueOf(TABLE_CONTENT));
		//创建列描述器
		HColumnDescriptor infoColumnDescriptor = new HColumnDescriptor("info");
		//设置块缓存
		infoColumnDescriptor.setBlockCacheEnabled(true);
		//设置块缓存大小
		infoColumnDescriptor.setBlocksize(2*1024*1024);
		//设置版本确界
		infoColumnDescriptor.setMinVersions(1);
		infoColumnDescriptor.setMaxVersions(1);
		//将列描述器添加到表描述器中
		contentTableDescriptor.addFamily(infoColumnDescriptor);
		//创建表
		admin.createTable(contentTableDescriptor);
		admin.close();
		connection.close();
	}

	/**
	 * 表名：ns_weibo:relation
	 * 列族名：attends，fans
	 * 列名：用户id
	 * value：用户id
	 * rowkey：当前操作人的用户id
	 * versions:1
	 * @throws IOException
	 */
	private void initTableRelation() {
		
	}
	/**
	 * 表名：ns_weibo:inbox
	 * 列族：info
	 * 列：当前用户所关注的人的用户id
	 * value：微博rowkey
	 * rowkey：用户id
	 * versions:100
	 * @throws IOException
	 */
	private void initTableInbox() throws IOException {
		
	}

	public static void main(String[] args) {
		
	}
}
