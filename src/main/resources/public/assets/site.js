$(document).ready(function () {
    $("#Products").on("change", function () {
        $list = $("#PermissionProfilesFiltered");
        $.ajax({
            url: "/a008/getPermissionProfiles",
            type: "GET",
            data: { productId: $("#Products").val() },
            traditional: true,
            dataType: "json",
            success: function (result) {
                console.log(result)
                $list.empty();
                $.each(result, function (i, item) {
                    console.log(item)
                    $list.append('<option value="' + item["permission_profile_id"] + '"> ' + item["permission_profile_name"] + ' </option>');
                });
            },
            error: function () {
                console.warn("Issues with calling the getPermissionProfiles endpoint. Please, check the solution.");
            }
        });
    });
});
