<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet">
<!-- 可选的Bootstrap主题文件（一般不使用） -->
<link href="http://apps.bdimg.com/libs/bootstrap/3.3.0/css/bootstrap-theme.min.css" rel="stylesheet">

<!-- HTML5 Shim 和 Respond.js 用于让 IE8 支持 HTML5元素和媒体查询 -->
<!-- 注意： 如果通过 file://  引入 Respond.js 文件，则该文件无法起效果 -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
<script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
<![endif]-->



<!-- 
静态资源必须放在WebContent下面
不能放在WEB-INF下面
因为WEB-INF下面的文件夹，tomcat做了安全访问的问题。
只有服务器才可以访问，客户端是没有权限访问的
就算在springmvc文件中配置了
servletMapping 映射路径： "/" 
 由于拦截了所有url，配置静态资源 
<mvc:default-servlet-handler />
也是没有用的
所以记住必须放在WebContent下面
-->