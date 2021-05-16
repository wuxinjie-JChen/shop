$(function() {
	$('.orders').DataTable(
			{
				order : [ [ 1, 'desc' ] ],
				ajax : {
					url : "/jcshopping/order/queryByParams",
					type : 'POST',
					dataSrc :  function(data){
						return data;
					}
				},
				columns : [ {
					data : "id"
				}, {
					data : "user.account"
				}, {
					data : "createTime"
				},  {
					data : "money"
				},  {
					data : "remark",
				}, {
					data : "state",
				} ],
				columnDefs : [ {
					targets : [ 2 ],
					orderable : false,
					render : function(data, type, row, meta) {
						
						const arr = row.createTime.split('T');
					    const d = arr[0];
					    const darr = d.split('-');

					    const t = arr[1];
					    const tarr = t.split('.000');
					    const marr = tarr[0].split(':');

					    const dd =
					      parseInt(darr[0]) +
					      '-' +
					      parseInt(darr[1]) +
					      '-' +
					      parseInt(darr[2]) +
					      ' ' +
					      parseInt(marr[0]) +
					      ':' +
					      parseInt(marr[1]) +
					      ':' +
					      parseInt(marr[2]);
					    return dd;
					}
				}, {
					targets: [5],
					render: function(data, type, row, meta) {
						if(data==0){
							return "未付款";
						}else if(data==1){
							return '<a href="javascript:;" onclick="fahuo('+row.id+')">发货</a>';
						}else if(data==2){
							return "已发货，未收货";
						}else if(data==3){
							return "已收货";
						}
						
					}
				}, {
					targets: [6],
					render: function(data, type, row, meta) {
						return '<a title="删除" href="javascript:;" onclick="order_del(' + row.id +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
					}
				}]
			});

});

function order_del(id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			//   /user/10
			url : "/jcshopping/order/delete?id="+id,
			type : "POST",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

function fahuo(id) {
	layer.confirm('确认发货？', function(index) {
		$.ajax({
			//   /user/10
			url : "/jcshopping/order/fahuo?id="+id,
			type : "POST",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}
