(function ($) {
    var getCsrf = function () {
        return $('[name=_csrf]').val();
    };
    var genFormSubmitParams = function (context) {
        var _this = $(context),
            data = {};
        _this.find('[name]').each(function (index, value) {
            var _this = $(this),
                name = _this.attr('name'),
                value = _this.val();
            data[name] = value;
        });
        var params = {};
        params["data"] = JSON.stringify(data);
        return params;
    };

    var bindFormSubmits = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            var params = genFormSubmitParams(this);
            var $form = $(this);
            $.ajax({
                url: $form.attr('action'),
                type: $form.attr('method'),
                contentType: "application/json",
                dataType: "json",
                data: params["data"],
                beforeSend: function (request) {
                    request.setRequestHeader("X-CSRF-TOKEN", getCsrf());
                    request.setRequestHeader("Accept", "application/json");
                },
                success: onSuccess,
                error: onError
            });
            return false;
        })
    };

    bindFormSubmits('organizationCreate', function (response) {
        console.log("New Organization Create");
        console.dir(response);
        alert("Organization create successful !");
        location.reload();
    }, function (xhr) {
        alert(xhr.status + "  Organization with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('classCreate', function (response) {
        console.log("newww");
        console.dir(response);
        alert("Class create successful !");
        location.reload();
    }, function (xhr) {
        alert(xhr.status + "  Class with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('classUpdate', function (response) {
        console.log("newww");
        console.dir(response);
        alert("Class updated successful !");
        location.reload();
    }, function (xhr) {
        alert(xhr.status + "  Class with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('studentCreate', function (response) {
        //alert("Student create successful !")
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        var responseError = JSON.parse(xhr.responseText);
        $('#errorField').html(responseError.errorField);
        $('#errorMessage').html(responseError.errorMessage);
        $('#errorSuggestion').html(responseError.errorSuggestion);
        $('#messageModal').modal('show');
    });

    bindFormSubmits('studentUpdate', function (response) {
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        alert(xhr.status + "Student Info Not Found !!!");
        //alert(xhr.responseText);
    });

    bindFormSubmits('studentAddContactInfo', function (response) {
        //alert("Student create successful !")
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        var responseError = JSON.parse(xhr.responseText);
        $('#errorField').html(responseError.errorField);
        $('#errorMessage').html(responseError.errorMessage);
        $('#errorSuggestion').html(responseError.errorSuggestion);
        $('#messageModal').modal('show');
    });

    bindFormSubmits('contactInfoUpdate', function (response) {
        window.location.replace("/student/" + $(".studentId").val() + "/details");
    }, function (xhr) {
        alert(xhr.status + "Student Contact Not Found !!!");
        //alert(xhr.responseText);
    });

    bindFormSubmits('enrolmentCreate', function (response) {
        alert("Enrolled !!");
        location.reload();
    }, function (xhr) {
        alert(xhr.status + " Student Enrolled Another Class !!!");
        //alert(xhr.responseText);
    });

})(jQuery);