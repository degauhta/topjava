var ajaxUrl = "ajax/meals/";
var datatableApi;

$(function () {
    datatableApi = $("#datatable").DataTable({
        "paging": false,
        "searching": false,
        "info": false,
        "columns": [
            {
                "data": "dateTime"
            },
            {
                "data": "description"
            },
            {
                "data": "calories"
            },
            {
                "defaultContent": "Edit",
                "orderable": false
            },
            {
                "defaultContent": "Delete",
                "orderable": false
            }
        ],
        "order": [
            [
                0,
                "desc"
            ]
        ]
    });
    makeEditable();
    // $('#startDate').datetimepicker();
});