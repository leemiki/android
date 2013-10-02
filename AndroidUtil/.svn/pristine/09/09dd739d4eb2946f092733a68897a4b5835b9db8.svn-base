package com.haolang.util.android.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.haolang.util.android.AndroidLog;

/**
 * SQLite操作类
 * <p>
 * v1.0支持对数据库的操作，打开，关闭，是否打开判断
 * </p>
 * <p>
 * v1.0支持对增删改查操作
 * </p>
 * 
 * @author wjl
 * 
 * @version 1.0 2013-7-22 13:44
 */
public class DatabaseProvider {
	public static final int DB_REABLE = 0;
	public static final int DB_WRITABLE = 1;

	private DatabaseHelper helper = null;
	private static DatabaseProvider provider = null;
	private SQLiteDatabase haolangDB = null;
	private String dbName = "SQL_TEST";
	private CursorFactory factory = null;
	private int version = 1;
	private static final String[] CONFLICT_VALUES = new String[] { "",
			" OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ",
			" OR REPLACE " };

	private DatabaseProvider(Context context) {
		helper = new DatabaseHelper(context, dbName, factory, version);
	}

	/**
	 * 单例模式获取provider实例
	 * 
	 * @param context
	 *            上下文对象
	 * 
	 * @return DatabaseProvider实例对象
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null，将报此异常
	 */
	public static DatabaseProvider getInstance(Context context) {

		if (context != null) {
			if (provider == null) {
				provider = new DatabaseProvider(context);
			}
			return provider;
		} else {
			throw new IllegalArgumentException("获取provider实例时传入的context为null");
		}
	}

	/**
	 * 打开数据库
	 * 
	 * @param type
	 *            获取的数据库类型,type值定义为： DB_REABLE: 获取可读数据库 DB_WRITABLE：获取可写数据库
	 * 
	 * @return 获取数据库是否成功，true为成功，false失败
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数不合法，将报此异常
	 */
	public boolean openDatabase(int type) {
		if (type == DB_REABLE) {
			haolangDB = helper.getReadableDatabase();
		} else if (type == DB_WRITABLE) {
			try {
				haolangDB = helper.getWritableDatabase();
			} catch (Exception e) {
				AndroidLog.w("DatabaseProvider", "打开可写数据库是发生异常，返回一个可读数据库");
				haolangDB = helper.getReadableDatabase();
			}
		} else {
			throw new IllegalArgumentException(
					"传入的数据库类型字段不合法，请直接引用DatabaseProvider中的字段");
		}

		return haolangDB.isOpen();
	}

	/**
	 * 判断数据库是否已经打开，如果数据库对象为null,则返回false
	 * 
	 * @return 数据库是否打开，true为已打开，false为未打开
	 */
	public boolean isOpen() {
		if (haolangDB != null) {
			return false;
		} else {
			return haolangDB.isOpen();
		}
	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		if (haolangDB != null && haolangDB.isOpen()) {
			haolangDB.close();
		}
	}

	/**
	 * 取得可读数据库
	 * 
	 * @return 返回一个可读的数据库
	 */
	public SQLiteDatabase getReadableDB() {
		if (haolangDB != null && !haolangDB.isOpen()) {
			haolangDB = helper.getReadableDatabase();
		}

		return haolangDB;
	}

	/**
	 * 取得可写数据库
	 * 
	 * @return 返回一个可写的数据库
	 */
	public SQLiteDatabase getWirtableDB() {
		if (haolangDB != null && !haolangDB.isOpen()) {
			haolangDB = helper.getWritableDatabase();
		}

		return haolangDB;
	}

	/**
	 * 通过sql语句执行增删改操作，适应多表之间的联合操作
	 * 
	 * @param sql
	 *            需要执行的sql语句
	 * 
	 * @return 成功与否标志，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean execSql(String sql) {
		if (sql == null || sql.equals("")) {
			Log.w("DatabaseProvider", "传入的sql语句为null，或为空");
			throw new IllegalArgumentException("执行sql语句是出入的sql语句不合法");
		} else {
			boolean isSucceed = true;

			try {
				haolangDB.execSQL(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				isSucceed = false;
			}

			return isSucceed;
		}
	}

	/**
	 * 根据sql语句创建表
	 * 
	 * @param sql
	 *            建表SQL
	 * 
	 * @return 返回建表是否成功，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean createTable(String sql) {
		if (sql == null || sql.equals("")) {
			Log.w("DatabaseProvider", "建表时传入的sql语句为null，或为空");
			throw new IllegalArgumentException("建表是出入的sql语句不合法");
		} else {
			boolean isSucceed = true;

			try {
				haolangDB.execSQL(sql);
			} catch (SQLException e) {
				e.printStackTrace();
				isSucceed = false;
			}

			return isSucceed;
		}
	}

	/**
	 * 数据源为Map形式，在指定表中插入数据
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            需要插入的数据，以Map存储
	 * @param conflictAlgorithm
	 *            数据发生冲突时处理方式标记位，具体定义在SQLiteDatabase类 分别可为： CONFLICT_NONE
	 *            不处理冲突时使用该项（不建议，除非能够确保插入的该条数据不会和之前数据冲突） CONFLICT_ABORT
	 *            发生错误时停止执行，但不回滚，默认使用该项  CONFLICT_ROLLBACK 发生错误时停止执行并回滚
	 *            CONFLICT_FAIL 该条数据插入失败，并继续往下执行 CONFLICT_IGNORE
	 *            发生冲突的该行数据将被忽略，不进行插入或者改变 CONFLICT_REPLACE
	 *            当在建表示时设置了唯一标示（UNIQUE），在发生冲突时，将替换掉该条数据
	 * 
	 * @return 插入操作是否成功，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean insertByMap(String table, Map<String, Object> values,
			int conflictAlgorithm) {
		if (table == null || table.equals("")) {
			Log.w("DatabaseProvider", "插入数据时传入的表名语句为null，或为空");
			throw new IllegalArgumentException("插入数据时传入的表名语句为null，或为空");
		} else if (values == null || values.size() == 0) {
			Log.w("DatabaseProvider", "插入数据时传入的数据源语句为null，或为空");
			throw new IllegalArgumentException("插入数据时传入的数据源语句为null，或为空");
		} else {
			boolean isSucceed = true;

			try {
				insetTable(table, values, conflictAlgorithm);
			} catch (Exception e) {
				e.printStackTrace();
				isSucceed = false;
			}

			return isSucceed;
		}
	}

	/**
	 * 拼接插入数据的sql语句并执行
	 * 
	 * @param table
	 *            表名
	 * @param values
	 *            需要插入的数据，以Map存储
	 * @param conflictAlgorithm
	 *            数据发生冲突时处理方式标记位
	 * 
	 * @throws SQLException
	 *             抛出的SQL语句异常
	 */
	private void insetTable(String table, Map<String, Object> values,
			int conflictAlgorithm) throws SQLException {
		haolangDB.acquireReference();
		try {
			StringBuilder sql = new StringBuilder();

			sql.append("INSERT");
			sql.append(CONFLICT_VALUES[conflictAlgorithm]);
			sql.append(" INTO ");
			sql.append(table);
			sql.append('(');

			int size = values.size();
			Object[] bindArgs = new Object[size];
			int i = 0;
			for (String colName : values.keySet()) {
				sql.append((i > 0) ? "," : "");
				sql.append(colName);
				bindArgs[i++] = values.get(colName);
			}

			sql.append(')');
			sql.append(" VALUES (");

			for (i = 0; i < size; i++) {
				sql.append((i > 0) ? ",?" : "?");
			}

			sql.append(')');

			haolangDB.execSQL(sql.toString(), bindArgs);
		} finally {
			haolangDB.releaseReference();
		}
	}

	/**
	 * 根据删除条件删除表中的数据
	 * 
	 * @param table
	 *            删除数据的表
	 * @param conditions
	 *            删除条件，用map封装，键为条件字段，值为条件字段的值
	 * 
	 * @return 返回删除操作是否成功，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean deleteByMap(String table, Map<String, Object> conditions) {
		if (table == null || table.equals("")) {
			Log.w("DatabaseProvider", "删除数据时传入的表名语句为null，或为空");
			throw new IllegalArgumentException("删除数据时传入的表名语句为null，或为空");
		} else if (conditions == null || conditions.size() == 0) {
			Log.w("DatabaseProvider", "删除数据时传入的条件语句为null，或为空");
			throw new IllegalArgumentException("删除数据时传入的条件语句为null，或为空");
		} else {
			boolean isSucceed = true;

			try {
				deleteTable(table, conditions);
			} catch (Exception e) {
				e.printStackTrace();
				isSucceed = false;
			}

			return isSucceed;
		}
	}

	/**
	 * 删除sql语句的拼接并执行
	 * 
	 * @param table
	 *            表名
	 * @param conditions
	 *            删除条件
	 * 
	 * @throws SQLException
	 *             抛出的SQL语句异常
	 */
	private void deleteTable(String table, Map<String, Object> conditions)
			throws SQLException {
		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM ");
		sql.append(table);
		sql.append(" WHERE ");

		int i = 0;
		int size = conditions.size();
		for (String clomn : conditions.keySet()) {
			Object value = conditions.get(clomn);
			if (value.getClass().getName().equals("java.lang.String")) {
				sql.append(clomn + "='" + value + "'");
			} else {
				sql.append(clomn + "=" + value);
			}
			if (i != size - 1) {
				sql.append(" and ");
			}
			i++;
		}

		haolangDB.execSQL(sql.toString());
	}

	/**
	 * 根据更新条件更新表
	 * 
	 * @param table
	 *            更新表的表名
	 * @param updates
	 *            更新数据,用Map封装数据源，键值分别对应更新字段以及更新字段的值
	 * @param conditions
	 *            更新条件，,用Map封装数据源，键值分别对应更新条件字段以及更新条件字段的值
	 * 
	 * @return 返回更新操作是否成功，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean updateByMap(String table, Map<String, Object> updates,
			Map<String, Object> conditions) {
		if (table == null || table.equals("")) {
			Log.w("DatabaseProvider", "更新数据时传入的表名语句为null，或为空");
			throw new IllegalArgumentException("更新数据时传入的表名语句为null，或为空");
		} else if (updates == null || updates.size() == 0) {
			Log.w("DatabaseProvider", "更新数据时传入的更新语句为null，或为空");
			throw new IllegalArgumentException("更新数据时传入的更新语句为null，或为空");
		} else if (conditions == null || conditions.size() == 0) {
			Log.w("DatabaseProvider", "更新数据时传入的条件语句为null，或为空");
			throw new IllegalArgumentException("更新数据时传入的条件语句为null，或为空");
		} else {
			boolean isSucceed = true;

			try {
				updateTable(table, updates, conditions);
			} catch (Exception e) {
				e.printStackTrace();
				isSucceed = false;
			}

			return isSucceed;
		}
	}

	/**
	 * 拼接更新语句并执行
	 * 
	 * @param table
	 *            表名
	 * @param updates
	 *            更新数据
	 * @param conditions
	 *            更新条件
	 * 
	 * @throws SQLException
	 *             抛出的SQL语句异常
	 */
	private void updateTable(String table, Map<String, Object> updates,
			Map<String, Object> conditions) throws SQLException {
		haolangDB.acquireReference();
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ");
		sql.append(table);
		sql.append(" SET ");

		int i = 0;
		int updatesSize = updates.size();
		for (String clomn : updates.keySet()) {
			Object value = updates.get(clomn);
			if (value.getClass().getName().equals("java.lang.String")) {
				sql.append(clomn + "='" + value + "'");
			} else {
				sql.append(clomn + "=" + value);
			}
			if (i != updatesSize - 1) {
				sql.append(" , ");
			}
			i++;
		}

		sql.append(" WHERE ");

		i = 0;
		int conditionsSize = updates.size();
		for (String clomn : conditions.keySet()) {
			Object value = conditions.get(clomn);
			if (value.getClass().getName().equals("java.lang.String")) {
				sql.append(clomn + "='" + value + "'");
			} else {
				sql.append(clomn + "=" + value);
			}
			if (i != conditionsSize) {
				sql.append(" and ");
			}
			i++;
		}

		haolangDB.execSQL(sql.toString());
	}

	/**
	 * 根据对象数组插入,要求对象的属性名与数据库中表字段名称相同，类型也需相同，切只支持基本类型
	 * 本函数进行了事务处理，有一条插入失败后会进行回滚操作
	 * 
	 * @param table
	 *            表名
	 * @param objects
	 *            对象数列
	 * @param clazz
	 *            对象的Class
	 * 
	 * @return 返回插入操作是否成功，成功为true，失败为false
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public boolean insertByObject(String table, List<?> objects, Class<?> clazz) {
		if (table == null || table.equals("")) {
			Log.w("DatabaseProvider", "插入数据时传入的表名语句为null，或为空");
			throw new IllegalArgumentException("插入数据时传入的表名语句为null，或为空");
		} else if (objects == null || objects.size() == 0) {
			Log.w("DatabaseProvider", "插入数据时传入的对象数列为null，或为空");
			throw new IllegalArgumentException("插入数据时传入的对象数列为null，或为空");
		} else {
			boolean isSucceed = true;
			haolangDB.beginTransaction();
			try {
				for (Object object : objects) {
					Map<String, Object> map = setObjectToMap(object, clazz);
					insertByMap(table, map, SQLiteDatabase.CONFLICT_REPLACE);
				}
				haolangDB.setTransactionSuccessful();
			} catch (IllegalArgumentException e) {
				isSucceed = false;
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				isSucceed = false;
				e.printStackTrace();
			} catch (SQLException e) {
				isSucceed = false;
				e.printStackTrace();
			} catch (InstantiationException e) {
				isSucceed = false;
				e.printStackTrace();
			} finally {
				haolangDB.endTransaction();
			}

			return isSucceed;
		}
	}

	/**
	 * 将object对象的属性映射到map中,对于boolean类型则存储int值，1为true，0为false
	 * 
	 * @param clazz
	 *            需要映射的对象
	 * 
	 * @return 映射完成后取得的map
	 * 
	 * @throws IllegalArgumentException
	 *             抛出的非法参数异常
	 * @throws IllegalAccessException
	 *             抛出的安全权限异常
	 * @throws InstantiationException
	 *             抛出的实例化异常
	 */
	private Map<String, Object> setObjectToMap(Object object, Class<?> clazz)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		Map<String, Object> map = new HashMap<String, Object>();

		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class<? extends Object> typeClass = field.getType();// 属性类型
			String key = field.getName();
			typeClass = field.getType();
			boolean isBasicType = isBasicType(typeClass);

			if (isBasicType) {
				field.setAccessible(true);
				Object value;

				if (typeClass.equals(Boolean.class)
						|| typeClass.equals(boolean.class)) {
					value = field.get(object);
					map.put(key, (Boolean) value ? 1 : 0);
				} else {
					value = field.get(object);
					map.put(key, value);
				}

			}

		}

		return map;
	}

	/**
	 * 判断是不是基本类型
	 * 
	 * @param typeClass
	 *            需要判断的类型
	 *            
	 * @return 返回是否是基本类型标示，true表示是基本类型，false表示非基本类型
	 */
	@SuppressWarnings("rawtypes")
	private boolean isBasicType(Class typeClass) {
		if (typeClass.equals(Integer.class) || typeClass.equals(int.class)
				|| typeClass.equals(Long.class) || typeClass.equals(long.class)
				|| typeClass.equals(Float.class)
				|| typeClass.equals(float.class)
				|| typeClass.equals(Double.class)
				|| typeClass.equals(double.class)
				|| typeClass.equals(Boolean.class)
				|| typeClass.equals(boolean.class)
				|| typeClass.equals(Short.class)
				|| typeClass.equals(short.class)
				|| typeClass.equals(String.class)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查询数据库，并将查询结果映射到Class类中
	 * 
	 * @param sql
	 *            查询的SQL语句
	 * @param clazz
	 *            将结果集映射到的类
	 * 
	 * @return 返回的结果，用List形式传回
	 * 
	 * @throws IllegalArgumentException
	 *             输入参数为null或空，将报此异常
	 */
	public List<Object> query(String sql, Class<?> clazz) {
		if (sql == null || sql.equals("")) {
			Log.w("DatabaseProvider", "查询数据时传入的SQL语句为null，或为空");
			throw new IllegalArgumentException("查询数据时传入的SQL语句为null，或为空");
		} else if (clazz == null) {
			Log.w("DatabaseProvider", "查询数据时传入的结果映射对象为null");
			throw new IllegalArgumentException("查询数据时传入的SQL语句为null，或为空");
		} else {
			List<Object> objects = new ArrayList<Object>();

			try {
				Cursor cursor = haolangDB.rawQuery(sql, null);

				if (cursor.moveToFirst()) {
					while (cursor.moveToNext()) {
						objects.add(setValueToClass(cursor, clazz));
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return objects;
		}
	}

	/**
	 * 将查询的结果映射到对象里
	 * 
	 * @param cursor
	 *            结果集游标
	 * @param clazz
	 *            对象的Class
	 *            
	 * @return 该对象的一个实例
	 * 
	 * @throws IllegalAccessException
	 *             抛出的安全权限异常
	 * @throws InstantiationException
	 *             抛出的实例化异常
	 */
	private Object setValueToClass(Cursor cursor, Class<?> clazz)
			throws IllegalAccessException, InstantiationException {
		List<String> columns = new ArrayList<String>(Arrays.asList(cursor
				.getColumnNames()));
		Field[] fields = clazz.getDeclaredFields();
		Object object = clazz.newInstance();

		for (Field field : fields) {
			String name = field.getName();
			if (columns.contains(name)) {
				field.setAccessible(true);
				Object value = getValue(field, cursor);
				field.set(object, value);
			}
		}

		return object;
	}

	/**
	 * 获取该属性值对应的查询结果，默认为String类型
	 * 
	 * @param field
	 *            对象属性
	 * @param cursor
	 *            结果集游标
	 * 
	 * @return 该属性的查询结果
	 */
	private Object getValue(Field field, Cursor cursor) {
		String columnName = field.getName();
		Class<? extends Object> typeClass = field.getType();

		if (typeClass.equals(Integer.class) || typeClass.equals(int.class)) {
			return cursor.getInt(cursor.getColumnIndex(columnName));
		} else if (typeClass.equals(Long.class) || typeClass.equals(long.class)) {
			return cursor.getLong(cursor.getColumnIndex(columnName));
		} else if (typeClass.equals(Float.class)
				|| typeClass.equals(float.class)) {
			return cursor.getFloat(cursor.getColumnIndex(columnName));
		} else if (typeClass.equals(Double.class)
				|| typeClass.equals(double.class)) {
			return cursor.getDouble(cursor.getColumnIndex(columnName));
		} else if (typeClass.equals(Boolean.class)
				|| typeClass.equals(boolean.class)) {
			return cursor.getInt(cursor.getColumnIndex(columnName)) == 1;
		} else if (typeClass.equals(Short.class)
				|| typeClass.equals(short.class)) {
			return cursor.getShort(cursor.getColumnIndex(columnName));
		} else {
			return cursor.getString(cursor.getColumnIndex(columnName));
		}
	}

}
