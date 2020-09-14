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
    components : { vuejsDatepicker },
    data: {
    	userType: "PROBA"
	},
	mounted (){
		axios
        .get('/users/log/test')
        .then(response => {
        	if(response.data == null)
        		this. userType='USER';
        	else{
        		if(response.data.typeOfUser == "Guest")
        			this. userType='GUEST';
        		else if(response.data.typeOfUser == "Host")
        			this.userType='HOST';
        		else 
        			this. userType = 'ADMIN';
        	}
        })
	},methods : {
		odjava : function(){
			axios
	          .get('/users/log/logout')
	          .then(response => {
	        		this.userType='USER';

	        });
			
		}
	}
});
