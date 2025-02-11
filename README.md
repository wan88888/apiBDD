# API BDD测试框架

## 项目概述
本项目是一个基于BDD (Behavior Driven Development) 的API测试框架，使用Cucumber和RestAssured实现自动化API测试。框架支持场景驱动的测试用例编写，JSON Schema验证，以及灵活的测试数据管理。

## 技术架构

### 核心技术栈
- Java 17
- Cucumber 7.11.1 - BDD测试框架
- Rest Assured 5.3.0 - API测试库
- JUnit 4.13.2 - 单元测试框架
- WireMock 3.0.1 - API模拟服务
- Spring Framework - 依赖注入和配置管理
- Lombok - 简化Java代码

### 主要特性
- 支持BDD风格的测试用例编写
- JSON Schema验证
- 灵活的测试数据管理
- 支持API模拟（Mock）
- 详细的测试报告生成

## 目录结构
```
src/
├── test/
│   ├── java/
│   │   └── com/api/
│   │       ├── automation/
│   │       │   └── utils/      # 工具类
│   │       └── testcase/
│   │           └── config/     # 测试配置
│   └── resources/
│       └── schema/            # JSON Schema文件
```

## 测试用例编写指南

### 1. 创建Feature文件
在`src/test/resources/features`目录下创建`.feature`文件：

```gherkin
Feature: 用户API测试

  Scenario: 获取用户信息
    Given 设置请求基础URL为"http://api.example.com"
    When 发送GET请求到"/users/1"
    Then 响应状态码应为200
    And 验证响应符合"user-schema.json"规范
```

### 2. 实现步骤定义
在`src/test/java`目录下创建对应的步骤定义类：

```java
@Given("设置请求基础URL为{string}")
public void setBaseUrl(String baseUrl) {
    // 实现步骤
}
```

### 3. JSON Schema验证
1. 在`src/test/resources/schema`目录下创建Schema文件
2. 使用`SchemaValidator`类进行验证：
```java
SchemaValidator.match(apiContext, "schema-file.json");
```

## 配置说明

### Maven配置
项目使用Maven管理依赖和构建。主要配置在`pom.xml`文件中：

- Java版本：17
- 测试线程数：1（可在pom.xml中修改）
- 测试报告输出目录：target/surefire-reports

### 运行测试
使用以下Maven命令运行测试：

```bash
mvn clean test
```

## 最佳实践

1. 测试用例组织
   - 按功能模块组织feature文件
   - 使用标签（@tags）分类测试用例
   - 保持场景描述简洁明确

2. 数据管理
   - 使用外部数据文件管理测试数据
   - 避免在步骤定义中硬编码测试数据

3. 验证策略
   - 优先使用JSON Schema进行响应验证
   - 对关键字段进行精确断言
   - 合理使用WireMock进行API模拟

## 注意事项

1. 运行环境要求
   - JDK 17或更高版本
   - Maven 3.6或更高版本

2. 常见问题解决
   - 确保JSON Schema文件位于正确的资源目录
   - 检查测试环境的网络连接
   - 查看日志文件获取详细错误信息

## 维护与更新

- 定期更新依赖版本
- 维护测试数据的有效性
- 及时更新文档以反映框架变化