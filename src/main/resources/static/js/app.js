(function ($) {

    $(".date").datepicker(
        {
            dateFormat: 'dd-mm-yy',
            changeYear: true,
            changeMonth: true,
            yearRange: '-50:+0'
        }
        );

    var getCsrf = function () {
        return $('[name=_csrf]').val();
    };
    var showLoader = function () {
        $(".loading-view").addClass('button_loader').attr("value", "");
    };
    var hideLoader = function () {
        $(".loading-view").removeClass('button_loader');
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
        data["feeInfoId"] = $(".feeInfoId").val();
        var params = {};
        params["data"] = JSON.stringify(data);
        console.log(params["data"]);
        return params;
    };

    var bindFormSubmits = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            var params = generateFormSubmitParams(this);
            var $form = $(this);
            showLoader();
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
        hideLoader();
        console.log("New Organization Create");
        console.dir(response);
        alert("Organization create successful !");
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "  Organization with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('feeInfoCreate', function (response) {
        hideLoader();
        console.dir(response);
        alert("Fee create successful !");
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "  Organization with given info exist");
    });

    bindFormSubmits('studentFeeNew', function (response) {
        hideLoader();
        console.dir(response);
        alert("Fee create successful !");
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "  Organization with given info exist");
    });

    bindFormSubmits('classCreate', function (response) {
        hideLoader();
        console.log("newww");
        console.dir(response);
        alert("Class create successful !");
        window.location.replace("/class/detail/" + response.id + "/year/" + new Date().getFullYear());
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "  Class with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('classUpdate', function (response) {
        hideLoader();
        console.log("newww");
        console.dir(response);
        alert("Class updated successful !");
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "  Class with given info exist");
        //alert(xhr.responseText);
    });

    bindFormSubmits('studentCreate', function (response) {
        hideLoader();
        //alert("Student create successful !")
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        hideLoader();
        var responseError = JSON.parse(xhr.responseText);
        $('#errorField').html(responseError.errorField);
        $('#errorMessage').html(responseError.errorMessage);
        $('#errorSuggestion').html(responseError.errorSuggestion);
        $('#messageModal').modal('show');
    });

    bindFormSubmits('studentUpdate', function (response) {
        hideLoader()
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "Student Info Not Found !!!");
        //alert(xhr.responseText);
    });

    bindFormSubmits('studentAddContactInfo', function (response) {
        hideLoader();
        //alert("Student create successful !")
        window.location.replace("/student/" + response.id + "/details");
    }, function (xhr) {
        hideLoader();
        var responseError = JSON.parse(xhr.responseText);
        $('#errorField').html(responseError.errorField);
        $('#errorMessage').html(responseError.errorMessage);
        $('#errorSuggestion').html(responseError.errorSuggestion);
        $('#messageModal').modal('show');
    });

    bindFormSubmits('contactInfoUpdate', function (response) {
        hideLoader();
        window.location.replace("/student/" + $(".studentId").val() + "/details");
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + "Student Contact Not Found !!!");
        //alert(xhr.responseText);
    });

    bindFormSubmits('enrolmentCreate', function (response) {
        hideLoader();
        alert("Enrolled !!");
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + " Student Enrolled Another Class !!!");
        //alert(xhr.responseText);
    });

    bindFormSubmits('subjectCreate', function (response) {
        hideLoader();
        alert("Subject added to class id" + response.classId);
        location.reload();
    }, function (xhr) {
        hideLoader();
        alert(xhr.status + " Subject cannot add to class");
        //alert(xhr.responseText);
    });

    var bindFormSubmitsWithUrl = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const className = $("#classId").val();
            console.log(className);
            const attendanceDate = $("#date").val();
            const url = "/attendances/ofClass/" + className + "/onDay/" + attendanceDate;
            console.log(url);
            showLoader();
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
        hideLoader();
        $("#show").empty()
        var table = "";
        $.each(response, function (i, itr) {
            table += '<tr>';
            table += '<td>' + itr.roleNumber + '</td>';
            table += '<td>' + itr.studentName + '</td>';
            if (itr.present == true) {
                table += '<td>' + 'Present' + '</td>';
            } else {
                table += '<td>' + 'Absent' + '</td>';
            }
            table += '</tr>';
        });
        $("#show").append(table);
    }, function (xhr) {
        hideLoader();
        console.dir(xhr);
    });

    var bindFormSubmitsWithUrl2 = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const url = "http://localhost:8080/exams/class/" + $('#clsId').find(":selected").val() + "/student/" + $('#studentId').val() + "/year/" + $('#year').val() + "/results";
            console.log(url);
            showLoader();
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

    var studentResultOnSuccess = function (data) {
        hideLoader();
        var h = ['QUIZ_1', 'QUIZ_2', 'FIRST_TERM', 'SECOND_TERM', 'FINAL'];
        console.log("Enter in Student Result On Success");

        $("#name").empty().append(data.studentName);
        $("#roll").empty().append(data.rollNumber);

        $("#result").empty();
        var table = "";

        for (var subject in data.examBySubject) {
            table += '<tr>';
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
    };

    bindFormSubmitsWithUrl2('studentResult', studentResultOnSuccess, function (xhr) {
        hideLoader();
        console.dir(xhr);
    });

    var bindFormSubmitsWithUrl3 = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const url = "http://localhost:8080/exams/class/" + $('#clsId').find(":selected").val() + "/subject/" + $('#subjectId').val() + "/year/" + $('#year').val() + "/results";
            console.log(url);
            showLoader();
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

    bindFormSubmitsWithUrl3('subjectResult', function (data) {
        hideLoader();
        var h = ['QUIZ_1', 'QUIZ_2', 'FIRST_TERM', 'SECOND_TERM', 'FINAL'];

        $("#subjectName").empty().append(data.subjectName);

        $("#result").empty();
        var table = "";

        $.each(data.student, function (index, obj) {
            table += '<tr>';
            table += '<td>' + obj.rollNumber + '</td>';
            table += '<td>' + obj.name + '</td>';
            for (var i = 0; i < h.length; i++) {
                var flag = false;
                var obtainMark = 0;
                $.each(obj.exams, function (index, obj) {
                    if (obj.examType === h[i]) {
                        flag = true;
                        obtainMark = Number(obj.obtainMark).toFixed(2);
                    }
                });
                table += '<td>' + obtainMark + '</td>';
            }
            table += '<td>' + Number(obj.result).toFixed(2) + '</td>';
            table += '</tr>';

        });

        $("#result").append(table);
    }, function (xhr) {
        hideLoader();
        console.dir(xhr);
    });

    var bindFormSubmitsWithUrl4 = function (formName, onSuccess, onError) {
        $('form.' + formName).on('submit', function () {
            const url = "http://localhost:8080/exams/class/" + $('#clsId').find(":selected").val() + "/year/" + $('#year').val() + "/results";
            console.log(url);
            showLoader();
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

    bindFormSubmitsWithUrl4('classResult', function (data) {
        hideLoader();
        if (data.length <= 0) return;
        $("#result").empty();
        var h = [];

        var table = "<table class=\"table table-striped\">";
        table += "<thead>";
        table += "<th>Roll No</th>";
        table += "<th>Student</th>";
        for (property in data[0].resultOfSubjects) {
            var val = property;
            h.push(val);
            table += "<th>" + val + "</th>";
        }
        table += "</thead>";
        console.dir(h);

        $.each(data, function (index, obj) {
            table += "<tr>";
            table += "<td>" + obj.roll + "</td>";
            table += "<td>" + obj.name + "</td>";
            Object.keys(h).forEach(function (key) {
                console.log();
                table += '<td>' + obj.resultOfSubjects[h[key]] + '</td>';
            });
            table += "</tr>";
        });

        table += '</table>';
        $("#result").append(table);

    }, function (xhr) {
        hideLoader();
        console.dir(xhr);
    });
})(jQuery);