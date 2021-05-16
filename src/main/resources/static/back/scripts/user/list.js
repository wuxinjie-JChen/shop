$(function() {
	$('.users').DataTable(
			{
				order : [ [ 1, 'desc' ] ],
				ajax : {
					url : "/jcshopping/user/queryAll",
					type : 'POST',
					dataSrc :  function(data){
						return data;
					}
				},
				columns : [ {
					data : "id"
				}, {
					data : "account"
				},  {
					data : "name",
				},{
					data : "headImg"
				},  {
					data : "email",
				}, {
					data : "phone",
				} ],
				columnDefs : [{
					targets : [ 0 ],
					orderable : false,
					render : function(id, type, row, meta) {
						return '<input id="input-' + id
								+ '" type="checkbox" name="ids" value=' + id
								+ '><label for="input-' + id + '"></label>';
					}
				}, {
					targets: [3],
					render: function(data, type, row, meta) {
						if(row.headImg==null){
							return '<img class="user_profile" src="../images/default_img.jpg" alt="user profile">';
						}else{
							return '<img class="user_profile" src="../'+row.headImg+'" alt="user profile">';
						}
						
					}
				}, {
					targets: [6],
					render: function(data, type, row, meta) {
						return '<a title="删除" href="javascript:;" onclick="user_del(' + row.id +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
					}
				}]
			});

});

function user_add(title, url, w, h) {
	layer_show(title, url, w, h);
}

function user_dels() {
	var cks = $(".users input[name='ids']:checked");
	layer.confirm('确认要删除吗？', function(index) {
		var param = [];
		cks.each(function() {
			param.push($(this).val());
		});
		$.ajax({
			url : "/user/" + param,
			type : "delete",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

function user_del(id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			//   /user/10
			url : "/jcshopping/user/delete?id="+id,
			type : "POST",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

/*function user_edit(id) {
	
	layer.open({
		type: 2,
		area: [800+'px', 510 +'px'],
		fix: false, //不固定
		maxmin: true,
		shade:0.4,
		title: "编辑教师",
		content: "/user_edit",
		success: function(layero, index){
			var body = layer.getChildFrame('body', index);
			$.ajax({
				url : "/user/" + id,
				type : "get",
				dataType : "json",
				success : function(data) {
					body.find('#uid').val(data.id);
					body.find('#username').val(data.name);
					body.find("input[type='radio'][value="+ data.gender +"]").attr("checked", "checked")
					body.find("#userType option[value="+ data.type +"]").attr('selected', 'selected');
					body.find('#desc').val(data.description);
				}
			});
		}
	});
	
}*/
