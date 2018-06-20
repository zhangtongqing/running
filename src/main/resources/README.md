######  打包前勾选要编译的环境，再运行package则在target目录生产对应环境的war包:
具体步骤：点击idea 右侧的 maven Projects 会看到如下目录：
Profiles
- [x] dev
- [ ] prod
- [ ] test

勾选对应的环境，再运行lifecycle下package命令，看到控制台输出如下信息，则打包成功
```
[INFO] Building war: D:\Work\target\api.war           //输出的war包路径
[INFO]
[INFO] --- spring-boot-maven-plugin:1.5.4.RELEASE:repackage (default) @ qdl ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 10.720s
[INFO] Finished at: Sat Apr 14 16:42:11 CST 2018
[INFO] Final Memory: 38M/91M
[INFO] ------------------------------------------------------------------------
```
