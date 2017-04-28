(function ($) {
    var getCsrf = function () {
        return $('[name=_csrf]').val();
    };
    var generateFormSubmitParams = function (context) {
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
        console.log(params["data"]);
        return params;
    };

    var bindFormSubmits = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            var params = generateFormSubmitParams(this);
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

    bindFormSubmits('subjectCreate', function (response) {
        alert("Subject added to class id" + response.classId);
        location.reload();
    }, function (xhr) {
        alert(xhr.status + " Subject cannot add to class");
        //alert(xhr.responseText);
    });

    var bindFormSubmitsWithUrl = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const className = $(".className").val();
            const attendanceDate = $("#date").val();
            const url = "/attendances/ofClass/" + className + "/onDay/" + attendanceDate;
            console.log(url);
            $.ajax({
                url: url,
                type: 'GET',
                contentType: "application/json",
                dataType: "json",
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

    bindFormSubmitsWithUrl('showAttendance', function (response) {
        $("#show").empty()
        var table = "";
        $.each(response, function (i, itr) {
            table += '<tr>';
            table += '<td>' + itr.roleNumber + '</td>';
            table += '<td>' + itr.studentName + '</td>';
            if (itr.present == true) {
                table += '<td>' + 'Present'+ '</td>';
            } else {
                table += '<td>' + 'Absent'+ '</td>';
            }
            table += '</tr>';
        });
        $("#show").append(table);
    }, function (xhr) {
        console.dir(xhr);
    });

    var bindFormSubmitsWithUrl2 = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const url = "http://localhost:8080/exams/class/" + $('#clsId').find(":selected").val() + "/student/" + $('#studentId').val() + "/year/" + $('#year').val() + "/results";
            console.log(url);
            $.ajax({
                url: url,
                type: 'GET',
                contentType: "application/json",
                dataType: "json",
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

    bindFormSubmitsWithUrl2('studentResult', function (data) {
        var h = ['QUIZ_1', 'QUIZ_2', 'FIRST_TERM', 'SECOND_TERM', 'FINAL'];

        $("#name").append(data.studentName);
        $("#roll").append(data.rollNumber);

//                $("#show").empty();
        var table = "";

        for (var subject in data.examBySubject) {
            table += '<tr>';
            console.log(subject);
            table += '<td>' + subject + '</td>'; // subject name
            for (var j = 0; j < h.length; j++) {
                var flag = false;
                var obtainMark;
                for (var i = 0; i < data.examBySubject[subject].length; i++) {
                    if (data.examBySubject[subject][i].examType === h[j]) {
                        flag = true;
                        obtainMark = data.examBySubject[subject][i].obtainMark;
                    }
                }
                if (flag) {
                    table += '<td>' + obtainMark + '</td>';
                } else {
                    table += '<td>' + '0' + '</td>';

                }
            }
            var totalmark = data.resultBySubject[subject];
            table += '<td>' + totalmark + '</td>';
            table += '</tr>';
        }
        $("#result").append(table);
    }, function (xhr) {
        console.dir(xhr);
    });



})(jQuery);