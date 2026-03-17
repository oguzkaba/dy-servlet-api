const messageModal = new bootstrap.Modal(document.getElementById('messageModal'), {
    keyboard: false
});
const errorTitle = 'Hata Mesajı';
const statusTitle = 'İşlem Durumu';
const warningTitle = 'Uyarı';
const context = '';
const imgSizeLimit = 307200;
const messageCountInterval = 5*60*1000;
const notificationCountInterval = 5*60*1000;

const downloadImageFile = document.querySelector('#imageFile');
const downloadImageShow = document.querySelector('#imageShow');
if(downloadImageFile && downloadImageShow){
    downloadImageFile.addEventListener('change',function(){
        [...this.files].map(file =>{
            if(file){
                const reader = new FileReader();
                reader.addEventListener('load',function(){
                    downloadImageShow.src = this.result;
                    downloadImageShow.nextElementSibling.classList.add('d-none');
                });
                reader.readAsDataURL(file);
            }else{
                downloadImageShow.src = context + '/assets/img/icon/cloud-upload.svg';
                downloadImageShow.nextElementSibling.classList.remove('d-none');
            }
        });
    });
}


angular.module('app_login',['ngCookies','app_custom_filters'])
    .controller('app_login_controller',function ($scope,$http,$cookies,$filter){
        $scope.userName = $cookies.get('login_username');
        $scope.password = $cookies.get('login_password');
        $scope.remember_me = $cookies.get('login_remember_me');
        $scope.role = $cookies.get('role');
        $scope.login = function (){
            let role;
            if($scope.role === true)
                role = 1;
            else
                role = 0;
            const userName = $scope.userName;
            $filter('toSHA1')($scope.password).then(function (result){
                if(result){
                    $http({
                        method: 'POST',
                        url: context + '/api/user/login',
                        data:{
                            userName: userName,
                            password: result,
                            role: role
                        }
                    }).then(function (response){
                        if(response.status === 200){
                            const responseData = response.data;
                            if(responseData.isSuccess){
                                if($scope.remember_me){
                                    $cookies.put('login_username', $scope.userName, { path:'/' });
                                    $cookies.put('login_password', $scope.password, { path:'/' });
                                    $cookies.put('login_remember_me', $scope.remember_me, { path:'/' });
                                }else{
                                    $cookies.put('login_username', '', { path:'/' });
                                    $cookies.put('login_password', '', { path:'/' });
                                    $cookies.put('login_remember_me', $scope.remember_me, { path:'/' });
                                }
                                $cookies.put('userID', responseData.userID, { path:'/' });
                                $cookies.put('role', role, { path:'/' });
                                $cookies.put('nameSurname', responseData.name + ' ' + responseData.surname, { path:'/' });
                                if(role === 0){
                                    $cookies.put('treatmentDoctor', responseData.treatmentDoctor, { path:'/' });
                                    let survey = responseData.surveyReplies;
                                    if(survey){
                                        survey = JSON.parse(survey);
                                        const basicProfile = {
                                                birthDayYear: survey['Doğum yılı'],
                                                patientWeight: survey['Kilo (kg)'],
                                                patientLength: survey['Boy(cm)'],
                                                patientGender: survey['Cinsiyet']
                                        };
                                        $cookies.put('basicProfile', JSON.stringify(basicProfile),{ path : '/' });
                                    }
                                    window.location.href = context + '/patient/main';
                                }else{
                                    $cookies.put('doctorID', responseData.id, { path:'/' });
                                    window.location.href = context + '/doctor/main';
                                }
                            }else{
                                $scope.modalTitle = errorTitle;
                                $scope.modalContent = responseData.message;
                                messageModal.toggle();
                            }
                        }
                    },function (error){
                       $scope.modalTitle = errorTitle;
                       $scope.modalContent = error.data;
                       messageModal.toggle();
                    });
                }
            })
        };
        $scope.roleToggle = function (){
            $scope.userName = '';
            $scope.password = '';
            $scope.role = !$scope.role;
        };
    });
angular.module('app_register',['app_custom_filters'])
    .controller('register_controller',function ($scope,$http,$filter){
        $scope.register = function (){
            $scope.modalTitle = errorTitle;
            if(!$scope.userName){
                $scope.modalContent = 'Hata: Kullanıcı adı boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if(!/^[a-zA-Z0-9]{3,11}$/.test($scope.userName)){
                $scope.modalContent = 'Hata: Kullanıcı harf, rakam, en az 3 en fazla 11 karakterden oluşabilir.'
                messageModal.toggle();
                return;
            }
            if(!$scope.name){
                $scope.modalContent = 'Hata: İsim boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if(!$scope.surname){
                $scope.modalContent = 'Hata: Soyisim boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if(!$scope.doctorCode){
                $scope.modalContent = 'Hata: Doktor kodu boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if(!/^[A-Z]{2}[a-zA-Z]{2,3}[0-9]{4,7}$/.test($scope.doctorCode)){
                $scope.modalContent = 'Hata: Geçersiz doktor kodu formatı.'
                messageModal.toggle();
                return;
            }
            if(!$scope.password){
                $scope.modalContent = 'Hata: Şifre boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if(!$scope.password_confirm){
                $scope.modalContent = 'Hata: Şifre(Tekrar) boş bırakılamaz.'
                messageModal.toggle();
                return;
            }
            if($scope.password !== $scope.password_confirm){
                $scope.modalContent = 'Hata: Şifre ve Şifre(Tekrar) aynı değil.'
                messageModal.toggle();
                return;
            }
            const maleBtn = document.querySelector('#maleBtn');
            const femaleBtn = document.querySelector('#femaleBtn');
            let gender;
            if(maleBtn.checked){
                gender = maleBtn.value;
            }else{
                gender = femaleBtn.value;
            }
            $filter('toSHA1')($scope.password).then(
                function (result){
                    console.log(result);
                    $http({
                        method: 'POST',
                        url: context + '/api/user/register',
                        data:{
                            tcNo: $scope.userName,
                            name: $scope.name,
                            surname: $scope.surname,
                            gender: gender,
                            doctorID: $scope.doctorCode,
                            password: result
                        }
                    }).then(function (response){
                        if(response.status === 200){
                            const responseData = response.data;
                            if(responseData.isSuccess){
                                $scope.userName = '';
                                $scope.name = '';
                                $scope.surname = '';
                                $scope.doctorCode = '';
                                $scope.password = '';
                                $scope.password_confirm = '';
                                $scope.modalTitle = statusTitle;
                                $scope.modalContent = 'Kayıt başarılı.';
                                messageModal.toggle();
                            }else{
                                $scope.modalTitle = errorTitle;
                                $scope.modalContent = responseData.message;
                                messageModal.toggle();
                            }
                        }
                    },function (error){
                        $scope.modalTitle = errorTitle;
                        $scope.modalContent = error.data;
                        messageModal.toggle();
                    });
                }
            )
        };
        $scope.openDialog = function (path){
            $scope.dContent = path;
        }
        $scope.termsAccept = function (){
            document.getElementById('termsAccept').checked = true;
        }
    });
angular.module('app_header',['app_custom_filters','ngCookies'])
    .controller('header_controller',function ($scope,$http, $interval,$rootScope,$cookies){
        $rootScope.showMessageCount = false;
        $rootScope.messageCount = 0;
        $rootScope.showNotificationCount = false;
        $rootScope.notificationCount = 0;
        $scope.messageList = [];
        $rootScope.messageContents = [];
        $rootScope.notificationList = [];
        $rootScope.notificationShow = false;
        const userID = $cookies.get('userID');
        $interval(function (){
            getUnReadMessageCount();
        },messageCountInterval);

        $interval(function (){
            getUnReadNotificationCount();
        },notificationCountInterval);
        $rootScope.messagesOpen = function (){
            $http({
                method: 'GET',
                url: context + '/api/messages/list?userID=' + userID + '&role=0'
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        $rootScope.messageList = responseData.messageList;
                        new bootstrap.Offcanvas(document.getElementById('messagesLayout'),{
                            keyboard: false
                        }).toggle();
                    }else{
                        $rootScope.modalTitle = errorTitle;
                        $rootScope.modalContent = responseData.message;
                        messageModal.toggle();
                    }
                }
            },function (error){
                $rootScope.modalTitle = errorTitle;
                $rootScope.modalContent = error.data;
                messageModal.toggle();
            });
        }

        $rootScope.notificationOpen = function (){
            $http({
                method: 'GET',
                url: context + '/api/notification/getAllNotification?userID=' + userID + '&role=0'
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        $rootScope.notificationList = responseData.logs;
                        new bootstrap.Offcanvas(document.getElementById('notificationLayout'),{
                            keyboard: false
                        }).toggle();
                    }else{
                        $rootScope.modalTitle = errorTitle;
                        $rootScope.modalContent = responseData.message;
                        messageModal.toggle();
                    }
                }
            },function (error){
                $rootScope.modalTitle = errorTitle;
                $rootScope.modalContent = error.data;
                messageModal.toggle();
            });
        }
        $rootScope.openNotificationItem = function (item){
            if(!item.isRead){
                $http({
                    method: 'POST',
                    url: context + '/api/notification/updateNotificationIsRead?notificationID=' + item.id + '&role=0&isRead=true'
                }).then(function (response){
                   if(response.status === 200){
                       const responseData = response.data;
                       if(responseData.isSuccess){
                           item.isRead = true;
                       }
                   }
                },function (error){});
            }
            $rootScope.notificationTitle = item.title;
            $rootScope.notificationBody = item.body;
            $rootScope.notificationShow = true;
        }
        $rootScope.closeNotificationItem = function (){
            $rootScope.notificationShow = false;
            $rootScope.notificationTitle = '';
            $rootScope.notificationBody = '';
        }
        function getUnReadMessageCount(){
            $http({
                method: 'GET',
                url: context + '/api/messages/unReadCount?userID=' + userID + '&role=0'
            }).then(function (response){
                if(response.status === 200){
                    const count = response.data;
                    if(count !== 0){
                        $rootScope.showMessageCount = true;
                        $rootScope.messageCount = count;
                    }else{
                        $rootScope.showMessageCount = false;
                    }
                }else{
                    $rootScope.showMessageCount = false;
                    $rootScope.messageCount = 0;
                }
            },function (error){
                console.log(error);
                $rootScope.showMessageCount = false;
                $rootScope.messageCount = 0;
            });
        }
        function getUnReadNotificationCount(){
            $http({
                method: 'GET',
                url: context + '/api/notification/getNotificationCount?userID=' + userID + '&role=0'
            }).then(function (response){
                if(response.status === 200){
                    const count = response.data;
                    if(count !== 0){
                        $rootScope.showNotificationCount = true;
                        $rootScope.notificationCount = count;
                    }else{
                        $rootScope.showNotificationCount = false;
                    }
                }else{
                    $rootScope.showNotificationCount = false;
                    $rootScope.notificationCount = 0;
                }
            },function (error){
                console.log(error);
                $rootScope.showNotificationCount = false;
                $rootScope.notificationCount = 0;
            });
        }
        getUnReadMessageCount();
        getUnReadNotificationCount();

    });
angular.module('app_full_img',[])
    .factory('imgFactory',function (){
        const obj = {};
        const full_img = document.querySelector('.full-img').querySelector('img');
        obj.fullscreen = function (index){
            if(index !== 'null'){
                const img = document.getElementById(index+'_img');
                full_img.src = img.src;
            }
            full_img.parentElement.classList.toggle('d-none');
        }
        return obj;
    });
angular.module('app_custom_filters',[])
    .filter('imgArrayToBase64',function (){
        return function (array){
            let binary = '';
            const newArray = new Uint8Array(array);
            const len = newArray.length;
            for (let i = 0; i < len; i++){
                binary += String.fromCharCode(newArray[i]);
            }
            return 'data:image/jpeg;base64, ' + window.btoa(binary);
        }
    })
    .filter('toDataSetJSON',function (){
        return function (dataSet){
            const newDataSet = [];
            const len = dataSet.length;
            for (let i = 0;  i < len; i++){
                newDataSet.push(
                    JSON.parse(dataSet[i])
                );
            }
            return newDataSet;
        }
    })
    .filter('imgBase64ToArray',function (){
        return function (data){
            const BASE64_MARKER = ';base64,';
            const parts = data.split(BASE64_MARKER);
            const raw = window.atob(parts[1]);
            const rawLength = raw.length;
            const newArray = [];
            for (let i = 0; i < rawLength; ++i) {
                newArray.push(raw.charCodeAt(i));
            }
            return newArray;
        }
    })
    .filter('textAbbreviation',function (){
        return function (text){
            if(text.length > 74){
                return text.substring(0,74) + '...';
            }else{
                return text;
            }
        }
    })
    .filter('toSHA1',function (){
        return async function(data){
            const msgUint8 = new TextEncoder().encode(data);
            const hashBuffer = await window.crypto.subtle.digest('SHA-1', msgUint8);
            const hashArray = Array.from(new Uint8Array(hashBuffer));
            return hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
        }
    })
    .filter('toSHA1s',function (){
        return async function(dataSet){
            if(dataSet){
                const len = dataSet.length;
                const toSha1Set = [];
                for (let i = 0; i < len; i++){
                    const data = dataSet[i];
                    const msgUint8 = new TextEncoder().encode(data);
                    const hashBuffer = await crypto.subtle.digest('SHA-1', msgUint8);
                    const hashArray = Array.from(new Uint8Array(hashBuffer));
                    const hashHex = hashArray.map(b => b.toString(16).padStart(2, '0')).join('');
                    toSha1Set.push(hashHex);
                }
                return toSha1Set;
            }
        }
    })
    .filter('changeDateLong',function (){
        return function (date){
            return Date.parse(date) + 3*60*60*1000;
        }
    })
    .filter('isWarningTitle',function (){
        return function (title){
            const result = title.toLocaleUpperCase('tr').indexOf('UYARI') !== -1;
            console.log(result);
            return result;
        }
    })
angular.module('patient_app_main',['app_header'])
    .controller('patient_app_main_controller',function ($scope,$http,$filter,$cookies){
        const profileImage = document.querySelector('#profilePictureImage');
        const userID = $cookies.get('userID');
        $scope.fullName = $cookies.get('nameSurname');
        $scope.bpList = [];
        $scope.bpListAvailable = false;
        let basicProfile = $cookies.get('basicProfile');
        if(basicProfile){
            basicProfile = JSON.parse(basicProfile);
            $scope.birthDayYear = basicProfile.birthDayYear;
            $scope.patientWeight =  basicProfile.patientWeight;
            $scope.patientLength = basicProfile.patientLength;
            $scope.patientGender = basicProfile.patientGender;
        }else{
            $http({
                method: 'GET',
                url: context + '/api/patient/getSurvey?patientID=' + userID
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        let survey = responseData.surveyReplies;
                        if(survey){
                            survey = JSON.parse(survey);
                            const basicProfile = {
                                birthDayYear: survey['Doğum yılı'],
                                patientWeight: survey['Kilo (kg)'],
                                patientLength: survey['Boy(cm)'],
                                patientGender: survey['Cinsiyet']
                            };
                            $scope.birthDayYear = basicProfile.birthDayYear;
                            $scope.patientWeight =  basicProfile.patientWeight;
                            $scope.patientLength = basicProfile.patientLength;
                            $scope.patientGender = basicProfile.patientGender;
                            $cookies.put('basicProfile', JSON.stringify(basicProfile), { path: '/' });
                            $scope.birthDayYear = basicProfile.birthDayYear;
                            $scope.patientWeight =  basicProfile.patientWeight;
                            $scope.patientLength = basicProfile.patientLength;
                            $scope.patientGender = basicProfile.patientGender;
                        }
                    }else{}
                }
            },function (error){});
        }
        $http({
            method: 'GET',
            url: context + '/api/user/profile/get?userID=' + userID + '&role=0'
        }).then(function (response){
            if(response.status === 200){
                const responseData = response.data;
                if(responseData.isSuccess){
                    const pp = responseData.profile.picture;
                    if(pp){
                        profileImage.src = $filter('imgArrayToBase64')(pp);
                    }else{
                        profileImage.src = context + '/assets/img/icon/profile_default.svg';
                    }
                }else{
                    $scope.modalContent = responseData.message;
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                    profileImage.src = context + '/assets/img/icon/profile_default.svg';
                }
            }
        },function (error){
            $scope.modalContent = error.data;
            $scope.modalTitle = errorTitle;
            messageModal.toggle();
        });
        $http({
            method: 'GET',
            url: context + '/api/reports/bloodPressure/get/limit?patientID=' + userID + '&limit=7'
        }).then(function (response){
            if(response.status === 200){
                const responseData = response.data;
                if(responseData.isSuccess){
                    $scope.bpList = responseData.bloodPressureList;
                    if($scope.bpList.length > 0){
                        $scope.bpListAvailable = true;
                    }
                }
            }
        },function (error){});
    });
angular.module('app_blood_pressure',['app_custom_filters','app_header'])
    .filter('isBloodPressureWarning',function (){
        return function (bpItem){
            return !(120 <= bpItem.majorValue && bpItem.majorValue <= 130 && 70 <= bpItem.minorValue && bpItem.minorValue <= 90);
        }
    })
    .filter('isPulseWarning',function (){
        return function (bpItem){
            return bpItem.pulseValue > 90;
        }
    })
    .filter('isBloodPressureAverageWarning',function (){
        return function (bpItem){
            const parseBP = bpItem.split(' / ');
            if(parseBP){
                if(parseBP.length === 2){
                    return parseFloat(parseBP[0]) > 135 || parseFloat(parseBP[1]) > 85;
                }
            }
            return false;
        }
    })
    .filter('isPulseAverageWarning',function (){
        return function (bpItem){
            return bpItem.pulseValue < 90;
        }
    })
    .controller('blood_pressure_controller',function ($scope,$http,$filter, $interval){
        $scope.minorValue = 80;
        $scope.majorValue = 124;
        $scope.pulseValue = 70;
        $http({
            method:"GET",
            url: context + "/api/patient/patientDoctorInformation?patientID=" + $scope.userID
        }).then(function (response){
            if(response.status === 200){
                const  responseData = response.data;
                if(responseData.isSuccess){
                    $scope.doctorID = responseData.information.doctorUserID;
                }else{
                    console.log(responseData);
                }
            }
        },function (error){
            console.log(error);
        });
        $scope.saveBloodPressure = function (){
            const requestBody = {
                patientID: $scope.userID,
                doctorID: $scope.doctorID,
                bloodPressure: {
                    minorValue: $scope.minorValue,
                    majorValue: $scope.majorValue,
                    pulseValue: $scope.pulseValue,
                    measureTime: -1
                }
            }
            $http({
                method: "POST",
                url: context + "/api/reports/updateBloodPressure",
                data: requestBody
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        $scope.minorValue = 80;
                        $scope.majorValue = 124;
                        $scope.pulseValue = 70;
                        if(responseData.message){
                            $scope.modalContent = responseData.message;
                            $scope.modalTitle = warningTitle;
                        }else{
                            $scope.modalContent = 'İşlem başarılı.';
                            $scope.modalTitle = statusTitle;
                        }
                    }else{
                        $scope.modalContent = responseData.message;
                        $scope.modalTitle = errorTitle;
                    }
                }
            },function (error){
                $scope.modalContent = error.data;
                $scope.modalTitle = errorTitle;
            });
            messageModal.toggle();
        }

        $scope.openBloodPressureHistory = function (){
            $http({
                method: "GET",
                url: context + "/api/reports/getBloodPressures?patientID=" +$scope.userID
            }).then(function (response){
                if(response.status === 200){
                    const  responseData = response.data;
                    if(responseData.isSuccess){
                        $scope.date = new Date();
                        const averageValue = averageValues(responseData.bloodPressureList);
                        $scope.averageBP = averageValue[0];
                        $scope.averagePulse = averageValue[1];
                        $scope.bpList = responseData.bloodPressureList;
                        $scope.isAvailable = $scope.bpList.length > 0;
                        if($scope.bpList.length > 0){
                            new bootstrap.Modal(document.getElementById("bloodPressureHistoryModal"),{
                                keyboard:  false
                            }).toggle();
                        }
                    }else{
                        $scope.modalContent = error.data;
                        $scope.modalTitle = errorTitle;
                        messageModal.toggle();
                    }
                }
            },function (error){
                $scope.modalContent = error.data;
                $scope.modalTitle = errorTitle;
               messageModal.toggle();
            });
        }
        $scope.removeHistory = function (){
            $scope.averageBP = '- / -';
            $scope.averagePulse = '-';
            $scope.isAvailable = false;
            $scope.bpList = [];
        }
        function averageValues(values){
            if(values){
                const len = values.length;
                let size = 0;
                let pulseSize = 0;
                let minorValueTotal = 0.0;
                let majorValueTotal = 0.0;
                let pulseValueTotal = 0.0;
                for (let i = 0;  i < len; i++){
                    const value = values[i];
                    if(!isLastSevenDay(value.reportDate)){
                        break;
                    }
                    minorValueTotal += value.minorValue;
                    majorValueTotal += value.majorValue;
                    if(value.pulseValue > 0){
                        pulseValueTotal += value.pulseValue;
                        pulseSize++;
                    }
                    size++;
                }
                if(size > 0){
                    const averageMinorValue = minorValueTotal / size;
                    const averageMajorValue = majorValueTotal / size;
                    const result = [];
                    result.push(averageMajorValue.toFixed(1) + ' / ' + averageMinorValue.toFixed(1));
                    if(pulseSize > 0){
                        const averagePulseValue = pulseValueTotal / pulseSize;
                        result.push(averagePulseValue.toFixed(1));
                    }else{
                        result.push('-');
                    }
                    return result;
                }
            }
            return ['- / -', '-'];
        }
        function isLastSevenDay(date){
            const longDate = new Date(date);
            const diff = $scope.date - longDate;
            const convertDate = diff/(24*60*60*1000);
            return convertDate <= 7;
        }
        $interval(function (){
            const currentDate = new Date();
            $scope.dateTime = $filter('date')(Number(currentDate),'dd-MM-yyyy HH:mm:ss');
        },1000);
    });


angular.module('patient_app_setting',['app_header'])
    .controller('app_setting_controller',function ($scope,$http,$filter,$cookies){
        const userID = $cookies.get('userID');
        $scope.fullName = $cookies.get('nameSurname');
        const profileImage = document.querySelector('#profilePictureImage');
        $http({
            method: 'GET',
            url: context + '/api/user/profile/get?userID=' + userID + '&role=0'
        }).then(function (response){
            if(response.status === 200){
                const responseData = response.data;
                if(responseData.isSuccess){
                    const pp = responseData.profile.picture;
                    if(pp){
                        profileImage.src = $filter('imgArrayToBase64')(pp);
                    }else{
                        profileImage.src = context + '/assets/img/icon/profile_default.svg';
                    }
                }else{
                    $scope.modalContent = responseData.message;
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                    profileImage.src = context + '/assets/img/icon/profile_default.svg';
                }
            }
        },function (error){
            $scope.modalContent = error.data;
            $scope.modalTitle = errorTitle;
            messageModal.toggle();
        })
        $scope.includeContent = function(contentPath){
            $scope.dContent = contentPath;
        }
        $scope.removeContent = function (){
            $scope.dContent = '';
        }
        $http({
            method: "GET",
            url: context + "/api/user/contact/information?userID=" + $scope.userID + "&role=0"
        }).then(function (response){
            const  responseData = response.data;
            if(responseData.isSuccess){
                $scope.contact = responseData.contact;
            }else{
                $scope.modalContent = responseData.message;
                $scope.modalTitle = errorTitle;
                messageModal.toggle();
            }
        },function (error){
            console.log(error);
        });
        $http({
            method:"GET",
            url: context + "/api/patient/patientDoctorInformation?patientID=" + $scope.userID
        }).then(function (response){
            if(response.status === 200){
                const  responseData = response.data;
                if(responseData.isSuccess){
                    $scope.information = responseData.information;
                }else{
                    $scope.modalContent = responseData.message;
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                }
            }
        },function (error){
            console.log(error);
        });
    })
    .controller('change_mail_controller',function ($scope,$http){
        $scope.changeMail = function (){
            const data = { userID: $scope.userID, role: 0, mail: $scope.mail};
            $http({
                method:"POST",
                url: context + '/api/user/update/mail',
                data: data
            }).then(function (response){
                const responseData = response.data;
                if(responseData.isSuccess){
                    $scope.contact.mail = $scope.mail;
                    $scope.mail = '';
                }else{
                    $scope.modalContent = responseData.message;
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                }
            },function (error){
                console.log(error);
            });
        }
    })
    .controller('change_phone_controller',function ($scope,$http){
        $scope.changePhone = function (){
            const data = { userID: $scope.userID, role: 0, phone: '0' + $scope.phone};
            $http({
                method:"POST",
                url: context + '/api/user/update/phone',
                data: data
            }).then(function (response){
                const responseData = response.data;
                if(responseData.isSuccess){
                    $scope.contact.phone = '0' + $scope.phone;
                    $scope.phone = '';
                }else{
                    $scope.modalContent = responseData.message;
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                }
            },function (error){
                console.log(error);
            });
        }
    })
    .controller('change_profile_picture',function ($scope,$http,$filter,$cookies){
        const userID = $cookies.get('userID');
        const profileImage = document.querySelector('#profilePictureImage');
        $scope.changeProfilePicture = function (){
            const files = document.querySelector('#imageFile').files;
            [...files].map(file =>{
                if(file){
                    if(file.size <= imgSizeLimit){
                        const reader = new FileReader();
                        reader.addEventListener('load',function (){
                            const imgBase64 = this.result;
                            const array = $filter('imgBase64ToArray')(imgBase64);
                            $http({
                                method: 'POST',
                                url: context + '/api/user/profile/update',
                                data:{
                                    userID: userID,
                                    role: 0,
                                    picture: array
                                }
                            }).then(function (response){
                                if(response.status === 200){
                                    const responseData = response.data;
                                    if(responseData.isSuccess){
                                        profileImage.src = imgBase64;
                                    }else{
                                        $scope.modalTitle = errorTitle;
                                        $scope.modalContent = responseData.message;
                                        messageModal.toggle();
                                    }
                                }
                            },function (error){
                                $scope.modalTitle = errorTitle;
                                $scope.modalContent = error.data;
                                messageModal.toggle();
                            })
                        });
                        reader.readAsDataURL(file);
                    }else{
                        $scope.modalTitle = warningTitle;
                        $scope.modalContent = `Resim boyutu en fazla ${imgSizeLimit / 1024}KB olabilir`;
                        messageModal.toggle();
                    }
                }
            })
        }
        $scope.removeProfilePicture = function (){
            $http({
                method: 'GET',
                url: context + '/api/user/profile/delete?userID=' + userID + '&role=0'
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        profileImage.src = context + '/assets/img/icon/profile_default.svg';
                        $scope.modalTitle = statusTitle;
                        $scope.modalContent = `Resim silme işlemi tamamlandı.`;
                        messageModal.toggle();
                    }else{
                        $scope.modalTitle = errorTitle;
                        $scope.modalContent = responseData.message;
                        messageModal.toggle();
                    }
                }
            },function (error){
               $scope.modalTitle = errorTitle;
               $scope.modalContent = error.data;
               messageModal.toggle();
            });
        }
    });
angular.module('patient_app_change_password',['app_header'])
        .controller('change_password_controller',function ($scope,$http,$filter){
            $scope.changePassword = function (){
                if(Boolean($scope.available_password) &&  Boolean($scope.new_password) &&  Boolean($scope.new_password_confirm)){
                    if($scope.new_password === $scope.new_password_confirm){
                        $filter('toSHA1s')([$scope.available_password,$scope.new_password]).then(function (results){
                            if(results){
                                const  requestData = {
                                    userID: $scope.userID,
                                    role: 0,
                                    password:  results[0],
                                    newPassword: results[1]
                                };
                                $http({
                                    method: 'POST',
                                    url: context + '/api/user/password/change',
                                    data: requestData
                                }).then(function (response){
                                    if(response.status === 200){
                                        const responseData = response.data;
                                        if(responseData.isSuccess){
                                            $scope.modalTitle = statusTitle;
                                            $scope.modalContent = 'İşlem başarılı';
                                            $scope.new_password_confirm = '';
                                            $scope.available_password = '';
                                            $scope.new_password = '';
                                        }else{
                                            $scope.modalTitle = errorTitle;
                                            $scope.modalContent = responseData.message;
                                        }
                                    }
                                    messageModal.toggle();
                                },function (error){
                                    $scope.modalTitle = errorTitle;
                                    $scope.modalContent = error.data;
                                    messageModal.toggle()
                                });
                            }
                        });
                    }else{
                        $scope.modalContent = 'Şifre ve Şifre(Tekrar) aynı değil.';
                        $scope.modalTitle = errorTitle;
                        messageModal.toggle();
                    }
                }else{
                    $scope.modalContent = 'Lütfen gerekli yerleri doldurunuz.';
                    $scope.modalTitle = errorTitle;
                    messageModal.toggle();
                }
            };
        });
angular.module('app_user_drug',['app_full_img','app_custom_filters','app_header'])
        .filter('nullControlFilter',function (){
            return function (item){
                const newArray = [];
                const size = item.length;
                for(let i = 0; i < size; i++){
                    if(item[i].drugText){
                        newArray.push(item[i].drugText);
                    }else{
                        newArray.push(item[i].imageFile);
                    }
                }
                return newArray;
            }
        })
    .controller('user_drug_list_controller',function ($scope,$http,$filter,imgFactory){
        $scope.userDrugHistoryList = [];
        $http({
            method: 'GET',
            url: context + '/api/patient/userDrug/get?userID=' + $scope.userID + '&role=0&patientID=' + $scope.userID
        }).then(function (response){
            if(response.status === 200){
                const responseData = response.data;
                if(responseData.isSuccess){
                    $scope.userDrugHistoryList = $filter('nullControlFilter')(responseData.userDrugList);
                }else{
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = responseData.message;
                    messageModal.toggle();
                }
            }
        },function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        });
        $scope.fullImageScreen = function (index){
            imgFactory.fullscreen(index);
        }
        $scope.removeServerUserDrugItem = function (id,index){
            $http({
                method: 'POST',
                url: context + '/api/patient/userDrug/remove',
                data:{
                    userID: $scope.userID,
                    id: id
                }
            }).then(function (response){
                const responseData = response.data;
                if(responseData.isSuccess){
                    if($scope.userDrugHistoryList.length > index){
                        $scope.userDrugHistoryList.splice(index,1);
                    }
                }else{
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = responseData.message;
                    messageModal.toggle();
                }
            },function (error){
                $scope.modalTitle = errorTitle;
                $scope.modalContent = error.data;
                messageModal.toggle();
            })
        };
    })
    .controller('user_drug_controller',function ($scope,$http,$filter,$timeout,imgFactory){
            $scope.addUserDrugList = [];
            $scope.userDrugHistoryList = [];
            $scope.isAvailable = false;
            $scope.addTextUserDrug = function (){
                if($scope.userDrugText){
                    $scope.addUserDrugList.push({
                        type: 1,
                        content: $scope.userDrugText
                    });
                    $scope.userDrugText = '';
                    $scope.isAvailable = true;
                }
            };
            $scope.addImageUserDrug = function (){
                const imageFile = document.querySelector('#imageFile');
                [...imageFile.files].map(file =>{
                    if(file){
                        if(file.size <= imgSizeLimit){
                            const  reader = new FileReader();
                            reader.addEventListener('load',function (){
                                const imgData = {
                                    type: 0,
                                    fileSource: this.result,
                                    fileName: file.name
                                };
                                $timeout(function (){
                                    $scope.addUserDrugList.push(imgData);
                                },1000);
                                $scope.isAvailable = true;
                            })
                            reader.readAsDataURL(file);
                        }else{
                            $scope.modalTitle = warningTitle;
                            $scope.modalContent = `Resim boyutu en fazla ${imgSizeLimit / 1024}KB olabilir`;
                            messageModal.toggle();
                        }
                    }
                });
            };
            $scope.fullImageScreen = function (index){
                imgFactory.fullscreen(index);
            }
            $scope.removeUserDrugItem = function (item){
                const index = $scope.addUserDrugList.indexOf(item);
                $scope.addUserDrugList.splice(index,1);
            }
            $scope.saveUserDrugs = function (){
                if($scope.addUserDrugList){
                    const size = $scope.addUserDrugList.length;
                    for (let i = 0; i < size; i++){
                        const item = $scope.addUserDrugList[i];
                        let drugData = null;
                        if(item.type === 0){
                            drugData = {
                                imageFile: {
                                    type: 0,
                                    fileName: item.fileName,
                                    fileSource: $filter('imgBase64ToArray')(item.fileSource)
                                }
                            }
                        }else{
                            drugData = {
                                drugText: item
                            }
                        }
                        $http({
                            method: 'POST',
                            url: context + '/api/patient/userDrug/add',
                            data:{
                                userID: $scope.userID,
                                userDrug: drugData
                            }
                        }).then(function (response){
                            if(response.status === 200){
                                const  responseData = response.data;
                                if(responseData.isSuccess){
                                    $scope.removeUserDrugItem(item);
                                }
                            }
                        },function (error){});
                    }
                }
            }
        });

angular.module('app_patient_examination',['ngMaterial', 'ngMessages','app_full_img','app_custom_filters','app_header'])
    .controller('examination_list_controller',function ($scope,$http,$filter,imgFactory){
        $scope.isAvailable = false;
        $scope.examinationList = [];
        $scope.imageFileList = [];
        $http({
            method: 'GET',
            url: context + '/api/reports/examinations/list?patientID=' + $scope.userID
        }).then(function (response){
            if(response.status === 200){
                const  responseData = response.data;
                if(responseData.isSuccess){
                    $scope.examinationList = responseData.examinations;
                    $scope.isAvailable = $scope.examinationList.length > 0;
                }else{
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = responseData.message;
                    messageModal.toggle();
                    $scope.isAvailable = false;
                }
            }
        },function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
            $scope.isAvailable = false;
        });
        $scope.examinationDetail = function (item,index){
            if(item){
                $http({
                    method: 'GET',
                    url: context + '/api/reports/examinations/contents?examinationID=' + item.id
                }).then(function (response){
                    if(response.status === 200){
                        const  responseData = response.data;
                        if(responseData.isSuccess){
                            $scope.imageFileList = responseData.imageFileList;
                            $scope.detailName = item.name;
                            $scope.detailDate = item.date;
                            $scope.detailID = item.id;
                            $scope.examinationIndex = index;
                            new bootstrap.Modal(document.getElementById('examinationDetail'),{
                                keyboard: false
                            }).toggle();
                        }else{
                            $scope.modalTitle = errorTitle;
                            $scope.modalContent = responseData.message;
                            messageModal.toggle();
                        }
                    }
                },function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                });
            }
        };
        $scope.fullImageScreen = function (index){
            imgFactory.fullscreen(index);
        }
        $scope.removeDetail = function (){
            $scope.imageFileList = [];
            $scope.detailName = '';
            $scope.detailDate = '';
            $scope.examinationIndex = null;
            $scope.detailID = null;
        }
        $scope.removeExamination = function (){
            if($scope.detailID){
                $http({
                    method: 'POST',
                    url: context + '/api/reports/examinations/remove',
                    data:{
                        userID: $scope.userID,
                        role: 0,
                        id: $scope.detailID
                    }
                }).then(function (response){
                    if(response.status === 200){
                        const responseData = response.data;
                        if(responseData.isSuccess){
                            $scope.examinationList.splice($scope.examinationIndex,1);
                            $scope.removeDetail();
                        }else{
                            $scope.modalTitle = errorTitle;
                            $scope.modalContent = responseData.message;
                            messageModal.toggle();
                        }
                    }
                },function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                });
            }
        }
    })
.controller('patient_examination_controller',function ($scope,$http,$timeout,$filter){
    $scope.isOpenExaminationDate = false;
    $scope.maxDate = new Date();
    $scope.examinationList = [];
    const imageFile = document.getElementById('imageFile');
    $scope.addExaminationImages = function (){
        [...imageFile.files].map(file =>{
            if(file){
                if(file.size <= imgSizeLimit){
                    const reader = new FileReader();
                    reader.addEventListener('load',function (){
                        const imgData = {
                            fileName: file.name,
                            fileSource: this.result,
                            type: 0
                        };
                        $timeout(function (){
                            $scope.examinationList.push(imgData);
                        },1000);

                    });
                    reader.readAsDataURL(file);
                }else{
                    $scope.modalTitle = warningTitle;
                    $scope.modalContent = `Resim boyutu en fazla ${imgSizeLimit / 1024}KB olabilir`;
                    messageModal.toggle();
                }
            }
        })
    };
    $scope.fullImageScreen = function (index){
        imgFactory.fullscreen(index);
    }
    $scope.removeItem = function (item){
        if(item){
            const index = $scope.examinationList.indexOf(item);
            if($scope.examinationList.length > index){
                $scope.examinationList.splice(index,1);
            }
        }

    };
    $scope.saveExamination = function (){
        $scope.modalTitle = errorTitle;
        if(!$scope.examinationName){
            $scope.modalContent = 'Lütfen tetkik ismi giriniz.';
            messageModal.toggle();
            return;
        }
        if(!$scope.examinationDate){
            $scope.modalContent = 'Lütfen tarih ekleyiniz.';
            messageModal.toggle();
            return;
        }
        if($scope.examinationList.length === 0){
            $scope.modalContent = 'Lütfen tetkik görüntüsü ekleyiniz.';
            messageModal.toggle();
            return;
        }
        $http({
            method: 'POST',
            url: context + '/api/reports/examinations/update',
            data:{
                userID: $scope.userID,
                name: $scope.examinationName,
                date: $filter('date')($scope.examinationDate, 'yyyy/MM/dd')
            }
        }).then(function (response){
            if(response.status === 200){
                const  responseData = response.data;
                if(responseData.isSuccess){
                    console.log(responseData.examinationID);
                    updateContents(responseData.examinationID);
                }else{
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = responseData.message;
                    messageModal.toggle();
                }
            }
        },function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        });
    }
    const updateContents = function (id){
        const  size = $scope.examinationList.length;
        for (let i = 0; i < size; i++){
            const  item = $scope.examinationList[i];
            $http({
                method: 'POST',
                url: context + '/api/reports/examinations/updateContent',
                data:{
                    id: id,
                    imageFile: {
                        type: 0,
                        fileName: item.fileName,
                        fileSource: $filter('imgBase64ToArray')(item.fileSource)
                    }
                }
            }).then(function (response){
                if(response.status === 200){
                    const  responseData = response.data;
                    if(responseData.isSuccess){
                        const index = $scope.examinationList.indexOf(item);
                        $scope.examinationList.splice(index,1);
                    }
                }
            },function (error){
                console.log(error.data);
            });
        }
    }
});

angular.module('app_patient_complaint',['ngMaterial','ngMessages','app_full_img','app_custom_filters','app_header'])
    .controller('patient_complaint_list_controller',function ($scope,$http,$filter,imgFactory){
        $scope.complaintList = [];
        $scope.isAvailable = false;
        $scope.complaintDetails = [];
        $http({
            method: 'GET',
            url: context + '/api/reports/complaints/list?patientID=' + $scope.userID
        }).then(function (response){
            if(response.status === 200){
                const responseData = response.data;
                if(responseData.isSuccess){
                    $scope.complaintList = responseData.complaints;
                    $scope.isAvailable = $scope.complaintList.length > 0;
                }else{
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = responseData.message;
                    messageModal.toggle();
                    $scope.isAvailable = false;
                }
            }
        },function (error){
           $scope.modalTitle = errorTitle;
           $scope.modalContent = error.data;
           messageModal.toggle();
           $scope.isAvailable = false;
        });
        $scope.complaintDetail = function (item,index){
            if(item){
                $http({
                    method: 'GET',
                    url: context + '/api/reports/complaints/contents?complaintID=' + item.id
                }).then(function (response){
                    if(response.status === 200){
                        const responseData = response.data;
                        if(responseData.isSuccess){
                            $scope.complaintDetails = $filter('toDataSetJSON')(responseData.contents);
                            console.log('Success');
                            $scope.detailDate = item.date;
                            $scope.detailID = item.id;
                            $scope.detailIndex = index;
                            new bootstrap.Modal(document.getElementById('complaintDetailModal'),{
                                keyboard: false
                            }).toggle();
                        }else{
                            $scope.modalTitle = errorTitle;
                            $scope.modalContent = responseData.message;
                            messageModal.toggle();
                        }
                    }
                },function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                })
            }
        };
        $scope.removeComplaint = function (){
            if($scope.detailID){
                $http({
                    method: 'POST',
                    url: context + '/api/reports/complaints/remove',
                    data:{
                        userID: $scope.userID,
                        id: $scope.detailID
                    }
                }).then(function (response){
                    if(response === 200){
                        const responseData = response.data;
                        if(responseData.isSuccess){
                            $scope.complaintList.splice($scope.detailIndex,1);
                            $scope.detailDate = null;
                            $scope.detailID = null;
                            $scope.detailIndex = null;
                        }else{
                            $scope.modalTitle = errorTitle;
                            $scope.modalContent = responseData.message;
                            messageModal.toggle();
                        }
                    }
                },function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                })
            }
        };
        $scope.fullImageScreen = function (index){
            imgFactory.fullscreen(index);
        }
    })
.controller('patient_complaint_controller',function ($scope,$http,$filter,$timeout, imgFactory){
    $scope.complaintList = [];
    $scope.isAvailable = false;
    $scope.maxDate = new Date();
    const imageFile = document.getElementById('imageFile');
    $scope.addComplaintText = function (){
        if($scope.complaintText){
            $scope.complaintList.push({
                type: 1,
                content: $scope.complaintText
            });
            $scope.complaintText = '';
            $scope.isAvailable = $scope.complaintList.length !== 0;
        }
    }
    $scope.addComplaintImage = function (){
        [...imageFile.files].map(file =>{
           if(file){
               if(file.size <= imgSizeLimit){
                   const reader = new FileReader();
                   reader.addEventListener('load',function (){
                       const imgData = {
                           type: 0,
                           fileName: file.name,
                           fileSource: this.result
                       };
                       $timeout(function (){
                            $scope.complaintList.push({
                                imgData
                            });
                       },1000);
                       $scope.isAvailable = true;
                   });
                   reader.readAsDataURL(file);
               }else{
                   $scope.modalTitle = warningTitle;
                   $scope.modalContent = `Resim boyutu en fazla ${imgSizeLimit / 1024}KB olabilir`;
                   messageModal.toggle();
               }
           }
        });
    }
    $scope.fullImageScreen = function (index){
        imgFactory.fullscreen(index);
    }
    $scope.removeComplaintItem = function (item){
        const index = $scope.complaintList.indexOf(item);
        $scope.complaintList.splice(index,1);
        $scope.isAvailable = $scope.complaintList.length !== 0;
    }
    $scope.saveComplaint = function (){
        if($scope.complaintList.length > 0){
            if($scope.complaintDate){
                $http({
                    method: 'POST',
                    url: context + '/api/reports/complaints/update',
                    data:{
                        date: $filter('date')($scope.complaintDate,'yyyy/MM/dd'),
                        userID: $scope.userID
                    }
                }).then(function (response){
                    if(response.status === 200){
                        const responseData = response.data;
                        if(responseData.isSuccess){
                            updateComplaintContents(responseData.id);
                        }else{
                            $scope.modalTitle = errorTitle;
                            $scope.modalContent = responseData.message;
                            messageModal.toggle();
                        }
                    }
                },function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                });
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = 'Lütfen şikayet başlangıç tarihi giriniz.';
                messageModal.toggle();
            }
        }else{
            $scope.modalTitle = errorTitle;
            $scope.modalContent = 'Lütfen şikayet içeriğini giriniz.';
            messageModal.toggle();
        }
    }
    function updateComplaintContents(complaintID){
        const len = $scope.complaintList.length;
        for (let i = 0; i < len; i++){
            const item = $scope.complaintList[i];
            let complaintRequestData = null;
            if(item.type === 0){
                complaintRequestData = {
                    type: 0,
                    fileName: item.fileName,
                    fileSource: $filter('imgBase64ToArray')(item.fileSource)
                };
            }else{
                complaintRequestData = item;
            }
            $http({
                method: 'POST',
                url: context + '/api/reports/complaints/updateContent',
                data:{
                    id:complaintID,
                    complaintContent: complaintRequestData
                }
            }).then(function (response){
                if(response.status === 200){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        removeComplaintItem(item);
                    }else{
                        $scope.modalTitle = errorTitle;
                        $scope.modalContent = responseData.message;
                        messageModal.toggle();
                    }
                }
            },function (error){
                $scope.modalTitle = errorTitle;
                $scope.modalContent = error.data;
                messageModal.toggle();
            });
        }
    }
    function removeComplaintItem(item){
        if(item){
            const index = $scope.complaintList.indexOf(item);
            if($scope.complaintList.length > index){
                console.log('silindi.');
                $scope.complaintList.slice(index,1);
            }
        }
    }
});
angular.module('app_patients',['app_header'])
    .filter('patientsFilter', function (){
        return function (model){
            const list = model['patients'];
            const keyMap = model['keyMap'];
            const isCheckedItem = model['isCheckedItem'];
            let code = model['code'];
            const newList = [];
            for(let i = 0; i< list.length; i++){
                const treatmentDoctor = list[i].treatmentDoctor;
                if(!code || isAvailableDoctorCode(code,treatmentDoctor)){
                    if(!isCheckedItem || isAvailable(keyMap, list[i])){
                        newList.push(list[i]);
                    }
                }
            }
            return newList;
        }
        function isAvailableDoctorCode(drCode,treatmentDoctor){
            if(treatmentDoctor !== null){
                const code = /[A-Za-z]{2}/.exec(treatmentDoctor)[0];
                return code === drCode;
            }else{
                return false;
            }
        }
        function isAvailable(keyMap,data){
            const survey = JSON.parse(data.survey);
            const warningDate = data.warningDate;
            const userDrug = data.isUserDrug;
            let result = true;
            let keys = Object.keys(keyMap);
            keys.forEach(key => {
                const value = keyMap[key];
                if(key === 'warning'){
                    result &= attributeAvailable(value, warningDate);
                }else if(key === 'userDrug'){
                    result &= attributeAvailable(value, userDrug);
                }else if(key === 'survey'){
                    result &= attributeAvailable(value, survey);
                }else{
                    if(survey !== null){
                        result = result && (survey[key].includes(keyMap[key]));
                    }else{
                        result = false;
                    }
                }
            })
            return result;
        }
        function attributeAvailable(value, data){
            let result = true;
            if(value !== 'null'){
                if(value === 'true'){
                    if(!data){
                        result = false;
                    }
                }else{
                    if(data){
                        result = false;
                    }
                }
            }
            return result;
        }
    })
.controller('app_patients_controller', function ($scope, $cookies, $http, $filter){
    let list = null;
    $scope.list = null;
    $scope.listCount = 0;
    $scope.surveyFilter = true;
    const filters = document.querySelectorAll('.filters');
    const allSelect = document.querySelector('#allSelect');
    let selectedCode = null;
    const userID = $cookies.get('userID');
    $http({
        method: 'GET',
        url: context + '/api/patient/list?userID=' + userID
    }).then(function (response){
        const responseData  = response.data;
        if(responseData.isSuccess){
            list = responseData.patientList;
            $scope.list = list;
            $scope.listCount = list.length;
        }else{
            $scope.modalTitle = errorTitle;
            $scope.modalContent = responseData.message;
            messageModal.toggle();
        }
    }, function (error){
        $scope.modalTitle = errorTitle;
        $scope.modalContent = error.data;
        messageModal.toggle();
    });
    for (let i = 0; i < filters.length; i++){
        filters[i].addEventListener('click', () => {
            PatientListRefresh(selectedCode);
        });
    }
    $scope.filterClick = function (){
        PatientListRefresh();
    }
    $scope.surveyFilterClick = function (value){
        $scope.surveyFilter = (value !== 'null') ? (value === 'true') ? true : false : true;
        PatientListRefresh();
    }
    $scope.categoryClick = function (code){
        selectedCode = code;
        PatientListRefresh();
    }
    $scope.sendMessage = function (){
        const patients = selectedPatients();
        console.log(patients);
        $http({
            method: 'POST',
            url: context + '/api/web/admin/notifications/send',
            data: {
                title: $scope.msgTitle,
                message: $scope.msgBody,
                userID: userID,
                patients: patients
            }
        }).then(function (response){
            const responseData = response.data;
            if(responseData.isSuccess){
                $scope.msgTitle = '';
                $scope.msgBody = '';
                $scope.modalContent = 'İşlem başarılı.';
                $scope.modalTitle = statusTitle;
                messageModal.toggle();
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = responseData.message;
                messageModal.toggle();
            }

        }, function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
        });
    }
    $scope.sendSurvey = function (){
        let error = null;
        if(!Boolean($scope.surveyBody)){
            error = 'Hata: Anket sorusu kısmı boş bırakılamaz.';
        }
        if(error !== null){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
            return;
        }
        const patients = selectedPatients();
        $http({
            method: 'POST',
            url: context + '/api/survey/send',
            data:{
                userID: userID,
                patients: patients,
                title: $scope.surveyTitle,
                question: $scope.surveyBody
            }
        }).then(function (response){
            const responseData = response.data;
            if(responseData.isSuccess){
                $scope.surveyBody = '';
                $scope.modalContent = 'İşlem başarılı.';
                $scope.modalTitle = statusTitle;
                messageModal.toggle();
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = responseData.message;
                messageModal.toggle();
                notErrorPatientUnChecked(responseData.errorPatients);
            }
        }, function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        })
    }
    function notErrorPatientUnChecked(errorPatients){
        if(errorPatients === null || errorPatients.length === 0){
            return;
        }
        const patients = document.querySelectorAll('#rowItem');
        for (let i = 0; i < patients.length; i++){
            if(patients[i].checked && !errorPatients.includes(patients[i].value)){
                patients[i].checked = false;
            }
        }
    }
    allSelect.addEventListener('click',() => {
        const items = document.querySelectorAll('#rowItem');
        items.forEach(item => {
            item.checked = false;
            const parent = item.parentNode.parentNode;
            if(parent && !parent.classList.contains('d-none')){
                item.checked = allSelect.checked;
            }
        });
    });
    function selectedPatients(){
        const patients = document.querySelectorAll('#rowItem');
        const patientList = [];
        for (let i = 0; i < patients.length; i++){
            if(patients[i].checked){
                patientList.push(patients[i].value);
            }
        }
        return patientList;
    }
    function PatientListRefresh(){
        let keyMap = {}
        let isCheckedItem = false;
        for(let i = 0;  i < filters.length; i++){
            if(filters[i].checked){
                let filterParse = filters[i].value.split('#');
                keyMap[filterParse[0]] = filterParse[1];
                isCheckedItem = true;
            }
        }
        if(allSelect !== null){
            allSelect.checked = false;
        }
        $scope.list = $filter('patientsFilter')({
            'patients': list,
            'keyMap': keyMap,
            'isCheckedItem': isCheckedItem,
            'code': selectedCode
        });
        $scope.listCount = $scope.list.length;
        console.log($scope.listCount);
    }
});
angular.module('doctor_register_app',['app_header'])
.controller('doctor_register_controller',function ($cookies, $http, $filter, $scope){
    $scope.doctorRegister = function (){
        let error = null;
        if(!Boolean($scope.tcNo)){
            error = 'Hata: T.C. Numarası boş bırakılamaz.';
        }else if(!Boolean($scope.name)){
            error = 'Hata: Ad kısmı boş bırakılamaz.';
        }else if(!Boolean($scope.surname)){
            error = 'Hata: Soyad kısmı boş bırakılamaz.';
        }else if(!Boolean($scope.doctorCode)){
            error = 'Hata: Doktor kodu boş bırakılamaz.';
        }else if(!/^([A-Z]{2}[A-Za-z]{2,3}[0-9]{4,7})$/.test($scope.doctorCode)){
            error = 'Hata: Doktor kodu formatı geçersiz.';
        }else if(!Boolean($scope.password)){
            error = 'Hata: Şifre kısmı boş bırakılamaz.';
        }else if(!Boolean($scope.password_confirm)){
            error = 'Hata: Şifre (Tekrar) kısmı boş bırakılamaz.';
        }else if(!Boolean($scope.password)){
            error = 'Hata: Şifreler aynı değil.';
        }
        /*
        else if($scope.mail === 'null'|null|''){
            error = 'Hata: Mail kısmı boş bırakılamaz.';
        }else if($scope.phone === 'null'|null|''){
            error = 'Hata: Telefon numarası boş bırakılamaz.';
        }
        */
        if(error !== null){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
            return;
        }
        let gender = 'Kadın';
        if(Boolean($scope.gender) === true){
            gender = 'Erkek';
        }
        $http({
            method: 'POST',
            url: context + '/api/doctor/register',
            data:{
                tcNo: $scope.tcNo,
                name: $scope.name,
                surname: $scope.surname,
                mail: $scope.mail,
                tel: $scope.phone,
                degree: $scope.degree,
                gender: gender,
                role: 1,
                doctorID: $scope.doctorCode,
                password: $scope.password
            }
        }).then(function (response){
            const responseData = response.data;
            if(responseData.isSuccess){
                $scope.tcNo = '';
                $scope.name = '';
                $scope.surname = '';
                $scope.mail = '';
                $scope.phone = '';
                $scope.doctorCode = '';
                $scope.password = '';
                $scope.password_confirm = '';
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = responseData.message;
                messageModal.toggle();
            }
        }, function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        });
    }
});
angular.module('app_doctors', ['app_header'])
.controller('app_doctors_controller', function ($http, $cookies, $scope){
    const userID = $cookies.get('userID');
    const allSelect = document.querySelector('#allSelect');
    $scope.list = null;
    $scope.listCount = 0;
    $http({
        method: 'GET',
        url: context + '/api/doctor/list?userID=' + userID
    }).then(function (response){
        const responseData = response.data;
        if(responseData.isSuccess){
            $scope.list = responseData.informationList;
            $scope.listCount = $scope.list.length;
        }else{
            $scope.modalTitle = errorTitle;
            $scope.modalContent = responseData.message;
            messageModal.toggle();
        }
    }, function (error){
        $scope.modalTitle = errorTitle;
        $scope.modalContent = error.data;
        messageModal.toggle();
    });
    allSelect.addEventListener('click',()=>{
        const items = document.querySelectorAll('#rowItem');
        for(let i = 0; i < items.length; i++){
            items[i].checked = allSelect.checked;
        }
    });
});
angular.module('app_admin_login',['app_header'])
.controller('app_admin_login_controller', function ($http, $scope, $cookies, $filter){
    $scope.userName = $cookies.get('admin_username');
    $scope.password = $cookies.get('admin_password');
    const rememberChecked = $cookies.get('admin_remember');
    if(rememberChecked === 'on'){
        $scope.rememberChecked = true;
    }else{
        $scope.rememberChecked = false;
    }
    $scope.login = function (){
        let error = null;
        if(!Boolean($scope.userName)){
            error = 'Hata: T.C. Kimlik numarası boş bırakılamaz.';
        }else if(!/[0-9]{11}/.test($scope.userName)){
            error = 'Hata: T.C. Kimlik numarası 11 hane ve sadece rakamlardan oluşabilir.';
        }else if(!Boolean($scope.password)){
            error = 'Hata: Şifre kısmı boş bırakılamaz.';
        }
        if(error !== null){
            console.log(error);
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
            return;
        }
        $http({
            method: 'GET',
            url: context + '/api/crypto/sha1?data=' + $scope.password
        }).then(function(response){
            const data = response.data;
            if(data.isSuccess){
                $http({
                    method: 'POST',
                    url: context + '/api/user/login',
                    data: {
                        userName: $scope.userName,
                        password: data.data,
                        role: 2
                    }
                }).then(function (response){
                    const responseData = response.data;
                    if(responseData.isSuccess){
                        if($scope.remember_me === 'on'){
                            $cookies.put('admin_login', $scope.userName,  { path:'/' });
                            $cookies.put('admin_password', $scope.password,  { path:'/' });
                            $cookies.put('admin_remember', $scope.remember_me,  { path:'/' });
                        }else{
                            $cookies.put('admin_login', '');
                            $cookies.put('admin_password', '');
                            $cookies.put('admin_remember', '');
                        }
                        $cookies.put('userID', responseData.userID,  { path:'/' });
                        $cookies.put('role', 2,  { path:'/' });
                        window.location.href = context + '/admin/control/main';
                    }else{
                        $scope.modalTitle = errorTitle;
                        $scope.modalContent = responseData.message;
                        messageModal.toggle();
                    }
                }, function (error){
                    $scope.modalTitle = errorTitle;
                    $scope.modalContent = error.data;
                    messageModal.toggle();
                });
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = data.message;
                messageModal.toggle();
            }
        }, function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        });
    }
});
angular.module('doctor_edit_app', ['app_header'])
.controller('doctor_edit_controller', function($http, $scope, $filter, $cookies){
    const userID = $cookies.get('userID');
    const url = new URL(window.location.href);
    const id = url.searchParams.get('id');
    $http({
        method: 'GET',
        url: context + '/api/doctor/information?doctorID=' + id
    }).then(function (response){
        const responseData = response.data;
        if(responseData.isSuccess){
            const information = responseData.information;
            $scope.mail = information.mail;
            $scope.phone = information.phone;
            $scope.doctorCode = information.doctorID;
            $scope.degree = information.degree;
            $scope.passwd = '';
            $scope.passwd_confirm = '';
        }else{
            $scope.modalTitle = errorTitle;
            $scope.modalContent = responseData.message;
            messageModal.toggle();
        }
    }, function (error){
        $scope.modalTitle = errorTitle;
        $scope.modalContent = error.data;
        messageModal.toggle();
    });

    $scope.doctorUpdate = function (){
        let error = null;
        /*
        if($scope.doctorCode === null|'null'|''){
            error = 'Hata: Doktor kodu boş bırakılamaz.';
        }else if(!/^([A-Z]{2}[A-Za-z]{2,3}[0-9]{4,7})$/.test($scope.doctorCode)){
            error = 'Hata: Doktor kodu en az 6 en fazla 11 haneden oluşur ve (A-Z, a-z, 0-9) karakterlerini kabul eder. Örnek: HTxx9900';
        }else
        * */
        const passwd = document.getElementById("passwd").value;
        const passwd_confirm = document.getElementById("passwd_confirm").value;
        console.log($scope.passwd);
        if(!Boolean($scope.degree)){
            error = 'Hata: Ünvan boş bırakılamaz.';
        }else if($scope.change_password === true){
            if(!Boolean(passwd)){
                error = 'Hata: Şifre boş bırakılamaz';
            }else if(!Boolean(passwd_confirm)){
                error = 'Hata: Şifre (Tekrar) boş bırakılamaz.';
            }else if(passwd !== passwd_confirm){
                error = 'Hata: Şifreler aynı değil.';
            }
        }
        if(error !== null){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
            return;
        }
        if($scope.change_password){
            update(passwd);
        }else{
            update(null);
        }
    }

    function update(password){
        $http({
            method: 'POST',
            url: context + '/api/doctor/update',
            data:{
                id: id,
                adminUserID: userID,
                degree: $scope.degree,
                password: password,
                phone: $scope.phone,
                mail: $scope.mail
            }
        }).then(function (response){
            const responseData = response.data;
            if(responseData.isSuccess){
                $scope.modalTitle = statusTitle;
                $scope.modalContent = 'İşlem başarılı';
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = responseData.message;
            }
            messageModal.toggle();
        }, function (error){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error.data;
            messageModal.toggle();
        })
    }
});
angular.module('admin_survey_add_app', ['app_header'])
.controller('admin_survey_add_controller', function ($http, $cookies, $scope){
    const userID = $cookies.get('userID');
    $scope.addSurvey = function (){
        let error = null;
        if(!Boolean($scope.surveyCode)){
            error = 'Hata: Kod kısmı boş bırakılamaz.';
        }else if(!Boolean($scope.surveyBody)){
            error =  'Hata: Anket içerik kısmı boş bırakılamaz.';
        }
        if(error !== null){
            $scope.modalTitle = errorTitle;
            $scope.modalContent = error;
            messageModal.toggle();
            return;
        }
        $http({
            method: 'POST',
            url: context + '/api/survey/byCode/add',
            data: {
                userID: userID,
                code: $scope.surveyCode,
                body: $scope.surveyBody
            }
        }).then(function (response){
            const data = response.data;
            if(data.isSuccess){
                $scope.surveyCode = '';
                $scope.surveyBody = '';
                $scope.modalTitle = statusTitle;
                $scope.modalContent = 'İşlem başarılı';
            }else{
                $scope.modalTitle = errorTitle;
                $scope.modalContent = data.message;
            }
            messageModal.toggle();
        }, function (error){
           $scope.modalTitle = errorTitle;
           $scope.modalContent = error.data;
           messageModal.toggle();
        });
    }
});