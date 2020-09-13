const HomePage = { template: '<home-page></home-page>' }
const login = { template: '<login></login>' }
const registration = { template: '<registration></registration>' }
const account = { template: '<account></account>' }


const router = new VueRouter({
	  mode: 'hash',
	  routes: [
	    { path: '/', component: HomePage},
	    { path: '/login', component: login },
	    { path: '/registration', component: registration },
	    { path: '/account', component: account }
	  ]
});

var app = new Vue({
	router,
	el: '#webApp',
    components : { vuejsDatepicker }
});

