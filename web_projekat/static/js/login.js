Vue.component("login", {
	data: function () {
	    return {
	    	logged : null,
	    	error:'',
	    	username: '',
	    	usernameError:'',
		    password:'',
		    passwordError:''
	    }
},
	template: ` 
<div id="logovanje">

<form v-on:submit.prevent="isValidForm" method="post">
	<table class="table" v-bind:hidden="logged">
		<tr>
			<td>Korisnicko ime:</td>
			<td><input class="input" placeholder="Unesite korisničko ime" type="text" v-model="username" name="username"/></td>	
			<td ><p style="color: red" >{{usernameError}}</p></td>	
		</tr>
		<tr>
			<td>Lozinka:</td>
			<td><input class="input" placeholder="Unesite lozinku" v-model="password" type="password" name="password"/></td>	
			<td ><p style="color: red" >{{passwordError}}</p></td>	
		</tr>
		<tr>
		<td colspan="2"><p style="color:red">{{error}}</p></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="submit"  value="Uloguj se"/></td>	
		</tr>

	</table>
	<p v-bind:hidden="!logged">Kosirnik je vec ulogovan!</p>
	<button v-bind:hidden="!logged" id="buttonBrisanje">Odjava</button><br />

</form>
</div>
		`, 
		
		
		
		mounted () {
	       axios
	        .get('/users/log/test')
	        .then(response => {
	        	if(response.data == null)
	        		this.logged = false;
	        	else
	        		this.logged = true;
	        })

	    },
		methods : {
			isValidForm : function() {
				this.error = '';
				this.usernameError='';
				this.passwordError='';
				
				if(this.username == "")
					this.usernameError =  'Molimo Vas da unesete korisničko ime!';
				else if(this.password == "")
					this.passwordError = 'Molimo Vas da unesete lozinku!';
				else
					{
					let data = {username: this.username, password : this.password};

					
					axios
			          .post('/users/login', JSON.stringify(data))
					  .then(response => {
						  if(response.data!=""){
							  window.location.href = "/";
						  }
						  else{
							  this.error = 'Unesite tačne podatke ili je korisnički nalog blokiran';
						  }

						  
						});
	      		  	
					
					}
			}
		}
});