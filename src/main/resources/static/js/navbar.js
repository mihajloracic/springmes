app.controller('navbarController', function($scope, $http) {
    $scope.asd = 'mihajlo'
    $scope.navbar = [
        {name: "pregledi kupaca",
            izvestaji:[]},
        {name: "jos neki izvestaji",
            izvestaji:[]},
        {name: "jako bitni izvestaji",
            izvestaji:[]}
    ];
});