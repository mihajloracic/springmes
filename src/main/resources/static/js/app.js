var app = angular.module('myApp', []);
app.run(function($rootScope) {
    $rootScope.color = 'blue';
    console.log($rootScope.color)
    $rootScope.byRange = function (fieldName, minValue, maxValue) {
        if (minValue === undefined || minValue === null) minValue = Number.MIN_VALUE;
        if (maxValue === undefined || maxValue === null) maxValue = Number.MAX_VALUE;

        return function predicateFunc(item) {
            return minValue <= item[fieldName] && item[fieldName] <= maxValue;
        };
    };
    $rootScope.greaterThan = function(prop, val,active){
        if(!active){
            return function(item){
                return true;
            }
        }
        return function(item){
            return item[prop] >= val;
        }
    }
    $rootScope.lessThan = function(prop, val,active){
        if(!active){
            return function(item){
                return true;
            }
        }
        return function(item){
            return item[prop] <= val;
        }
    }
    $rootScope.removeZero = function(prop,active){
        if(!active){
            return function(item){
                return true;
            }
        }
        return function(item){
            return item[prop] != 0;
        }
    }
    $rootScope.orderByMe = function(x) {
        $rootScope.myOrderBy = x;
        $rootScope.ascending = !$rootScope.ascending;
    }
    $rootScope.getDateFormat = function(date){
        return date.getFullYear()+'.'+date.getMonth()+'.'+date.getDate()
    }
});