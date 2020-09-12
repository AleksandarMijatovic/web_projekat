Vue.component("login", {
	
	template: ` 
<div id="logovanje">

<form v-on:submit.prevent="checkFormValid" method="post">
	<table class="table" v-bind:hidden="logged">
		<tr>
			<td>Korisnicko ime:</td>
			<td><input class="input" placeholder="Unesite korisnicko ime" type="text" /></td>	
		
		</tr>
		<tr>
			<td>Lozinka:</td>
			<td><input class="input" placeholder="Unesite lozinku" /></td>	
			
		</tr>

		<tr>
			<td colspan="2" align="center"><input type="submit"  value="Uloguj se"/></td>	
		</tr>

	</table>
	

</form>
</div>
		`
});