$(function() {
	$('.goods').DataTable(
			{
				order : [ [ 1, 'desc' ] ],
				ajax : {
					url : "/jcshopping/good/find",
					type : 'POST',
					dataSrc :  function(data){
						return data;
					}
				},
				columns : [ {
					data : "id"
				}, {
					data : "name"
				},  {
					data : "price",
				},{
					data : "img"
				},  {
					data : "classId",
				}, {
					data : "num",
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
						return '<img class="good_profile" src="'+row.img+'">';
					}
				},  {
					targets: [4],
					render: function(data, type, row, meta) {
						return row.category.classes;
					}
				}, {
					targets: [6],
					render: function(data, type, row, meta) {
						return '<a title="删除" href="javascript:;" onclick="good_del(' + row.id +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
					}
				}]
			});

});

function user_add(title, url, w, h) {
	layer_show(title, url, w, h);
}

function good_del(id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			//   /user/10
			url : "/jcshopping/good/delete?id="+id,
			type : "POST",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

