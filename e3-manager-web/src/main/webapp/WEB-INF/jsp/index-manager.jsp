<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<jsp:include page="${pageContext.request.contextPath }/layout/inc.jsp"></jsp:include>
</head>
<body>
	<div style="padding: 10px 10px 10px 10px">
		<div style="padding: 5px">
			<a href="javascript:void(0)" class="easyui-linkbutton"
				onclick="dataImport();">索引维护</a>
		</div>
	</div>
	<script type="text/javascript">
		function dataImport() {
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/item/dataImport',
				success : function(data) {
					if (data.status == 200) {
						$.messager.alert('提示', '恭喜，导入数据成功!');
					} else {
						$.messager.alert('提示', '网络异常，请稍后再试!');
					}
				}
			});
		}
		</script>
	</body>
	</html>
	