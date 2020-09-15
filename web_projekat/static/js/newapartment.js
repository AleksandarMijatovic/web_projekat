
Vue.component("apartment", {
	data: function () {
		    return {
		    	placesAutocomplete:null,
		    	apartmentType: '',
		    	apartmentTypeError: '',
		    	numberOfRooms: '',
		    	numberOfRoomsError: '',
		    	numberOfGuests: '',
		    	numberOfGuestsError: '',
		    	price: '',
		    	priceError: '',
		    	checkInTime: '',
		    	checkInTimeError: '',
		    	checkOutTime: '',
		    	checkOutTimeError: '',
		        amenities: null,
		        checkedAmenities: {},
		        selectedAmenities: [],
		        dateFrom: new Date(Date.now() + 24*60*60*1000),
		        dateFromError: '',
		        dateTo: '',
		        dateToError: '',
		        city: '',
		        zip:'',
		        street:'',
		        streetError:'',
		        streetNumber:'',
		        streetNumberError:'',
		        longitude:'',
		        latitude:'',
		        images: [],
                imagesForBackend: [],
                imageSize: '40%',
                imageCount: 0,
                width:window.screen.availWidth/5.5,
                disabledDates:{to: new Date()}

			    }
	},
	template: ` 
<div>

<form v-on:submit.prevent="checkFormValid" method="post">
<table  style="width:100%">
<tr>
<td style="width:50%">
	<table class="table"  style="width:60%">
		<tr>
			<td>Tip apartmana:</td>
			<td>
  				<div class="pol"><input type="radio" name="apartmentType" v-model="apartmentType" value="room"> Soba<br></div>
  				<div class="pol"><input type="radio" name="apartmentType" v-model="apartmentType" value="apartment"> Apartman<br></div>
			</td>
			<td ><p style="color: red" >{{apartmentTypeError}}</p></td>	
		</tr>
		<tr>
			<td>Broj soba:</td>
			<td><input class="input" placeholder="Unesite broj soba" type="number" min="1" v-model="numberOfRooms" name="numberOfRooms"/></td>
			<td ><p style="color: red" >{{numberOfRoomsError}}</p></td>	
		</tr>
		<tr>
			<td>Broj gostiju:</td>
			<td><input class="input" placeholder="Unesite broj gostiju" type="number" min="1" v-model="numberOfGuests" name="numberOfGuests"/></td>
			<td ><p style="color: red" >{{numberOfGuestsError}}</p></td>	
		</tr>
		<tr>
			<td>Cena po noci:</td>
			<td><input class="input" placeholder="Unestice cenu" type="number" min="0" v-model="price" name="price"></td>
			<td>din</td>
			<td ><p style="color: red" >{{priceError}}</p></td>	
		</tr>
		<tr>
			<td>Vreme za prijavu:</td>
			<td><input class="input" placeholder="Unestice cenu" type="time" v-model="checkInTime" name="checkInTime"></td>
			<td ><p style="color: red" >{{checkInTimeError}}</p></td>	
		</tr>
		<tr>
			<td>Vreme za odjavu:</td>
			<td><input class="input" placeholder="Unestice cenu" type="time" v-model="checkOutTime" name="checkOutTime"></td>
			<td ><p style="color: red" >{{checkOutTimeError}}</p></td>	
		</tr>
		<tr>
			<td>Datum od:</td>
			<td><vuejs-datepicker placeholder="Unesite pocetni datum" :disabled-dates="disabledDates" v-model="dateFrom" ></vuejs-datepicker></td>
			<td ><p style="color: red" >{{dateFromError}}</p></td>	
		</tr>
		<tr>
			<td>Datum do:</td>
			<td><vuejs-datepicker placeholder="Unesite krajnji datum" :disabled-dates="disabledDates" v-model="dateTo" ></vuejs-datepicker></td>
			<td ><p style="color: red" >{{dateToError}}</p></td>	
		</tr>
		<tr>
			<td colspan="2"><h3>Unesite lokaciju:</h3></td>
		</tr>
		<tr>
			<td colspan="2">
			<div class="form-group">
	    		<label for="form-address">Adresa</label>
	    		<input type="search" class="form-control" id="form-address" placeholder="Unesite adresu" />
			</div>
			<br/>
			<div >
	    		<label >Broj:</label>
	    		<input type="number" min="1" v-model="streetNumber" name="streetNumber" class="form-control"  placeholder="Unesite broj" />
	    		<p style="color: red" >{{streetNumberError}}</p>
			</div>
			<br/>
			<div class="form-group">
		    	<label for="form-city">Ulica:</label>
		    	<input type="text" class="form-control" v-model="street" name="street" id="form-street">
			</div>
			<br/>

			<div class="form-group">
		    	<label for="form-city">Grad:</label>
		    	<input type="text" class="form-control" v-model="city" name="city"  id="form-city">
			</div>
			<br/>
			<div class="form-group">
		    	<label for="form-zip">Postanski broj:</label>
		    	<input type="text" class="form-control" v-model="zip" name="zip" id="form-zip">
			</div>
			<br/>
			<div class="form-group">
		    	<label for="form-longitude">Geografska duzina:</label>
		    	<input type="text" class="form-control" v-model="longitude" name="longitude" id="form-longitude">
			</div>
			<br/>
			<div class="form-group">
				<label for="form-latitude">Geografska sirina</label>
		    	<input type="text" class="form-control"  v-model="latitude" name="latitude" id="form-latitude">
			</div>
			<div>
				<p style="color: red" >{{streetError}}</p>
			</div>
			</td>
		</tr>

	</table>
</td>
<td style="vertical-align:top">
	<div style="float:left">
	<h1>Sadrzaj apartmana:</h1>
	
	<table class="tableAmenities">
		<tr v-for="(amenity, index) in amenities">
			<input type="checkbox" v-bind:value="amenity" v-model="selectedAmenities" :value="amenity"/>
          {{amenity.name}}
          </br>
        </tr>
	</table>
	</div>
</td>
</tr>
</table>	    
  
<input v-if="imageCount < 5" type="file" @change="onFileChange" />
        <input v-else type="file" @change="onFileChange" disabled="true"/>
 
 
    <table>
        <tr>
            <td v-for="(url, index) in images"  >
                <img :src="url" :width="width" v-on:click="deleteImage(index)" />
            </td>
        </tr>
    </table>

			<td colspan="3" align="center"><input type="submit"  value="Unesi apartman"/></td>

</form>	

  </div>
</div>
`,components : { 
	vuejsDatepicker
},
	mounted(){
	axios
    .get('/amenity')
    .then(response => (this.amenities = response.data)),
		axios
		.get("/users/log/test")
		.then(response => {
			if(response.data == null){
	      		  window.location.href = "#/login";
			}
		});
	
	
	}, 
	methods : {	
		onFileChange(e) {
            const file = e.target.files[0];
            this.createBase64Image(file);
            this.imageCount++;
            this.images.push(URL.createObjectURL(file));
        },
        computedWidth: function () {
            return window.screen.availWidth/5;
          },
        createBase64Image(file){
            const reader= new FileReader();
           
            reader.onload = (e) =>{
                this.imagesForBackend.push(e.target.result);
            }
            reader.readAsDataURL(file);
        },
 
        deleteImage(index){
            this.imageCount--;
            this.images.splice(index,1);
            this.imagesForBackend.splice(index,1);
        },
		checkFormValid : function() {
			this.checkInTimeError = '';
			this.checkOutTimeError = '';
			this.dateToError = '';
			this.dateFromError = '';
			this.apartmentTypeError='';
			this.numberOfRoomsError='';
			this.numberOfGuestsError='';
			this.priceError='';
			this.checkInTimeError='';
			this.streetError = '';
			this.streetNumberError='';
			
		    if(this.apartmentType == "")
				this.apartmentTypeError =  'Tip apartmana je obavezno polje!';
			else if(this.numberOfRooms == "")
				this.numberOfRoomsError =  'Broj soba je obavezno polje!';
			else if(this.numberOfGuests == "")
				this.numberOfGuestsError =  'Broj gostiju je obavezno polje!';
			else if(this.price == "")
				this.priceError =  'Broj gostiju je obavezno polje!';
			else if(this.checkInTime == "")
				this.checkInTimeError =  'Vreme za prijavu gostiju je obavezno polje!';
			else if(this.checkOutTime == "")
				this.checkOutTimeError =  'Vreme za prijavu gostiju je obavezno polje!';
			else if(this.dateFrom == "")
				this.dateFromError =  'Pocetno vreme za rezervaciju je obavezno polje';
			else if(this.dateTo == "")
				this.dateToError =  'Krajnje vreme za rezervaciju je obavezno polje!';
			else if(this.dateTo.getTime() <= this.dateFrom.getTime())
				this.dateToError =  'Datum do mora biti veci od datuma od!';
			else if(this.streetNumber == "")
				this.streetNumberError = 'Broj ulice je obavezno polje!';
			else
				{
				
					//let period= { dateFrom:dateFrom, dateTo:dateTo }
					let adressLocation = { city:this.city,postNumber:this.zip,street:this.street,  streetNumber:parseInt(this.streetNumber)};
					
					let dateFrom = (new Date(this.dateFrom.getFullYear(),this.dateFrom.getMonth() , this.dateFrom.getDate())).getTime(); 
					let dateTo = (new Date(this.dateTo.getFullYear(),this.dateTo.getMonth() , this.dateTo.getDate())).getTime(); 
					let period = [ { dateFrom: dateFrom , dateTo: dateTo }];

										
					let location = { adress:adressLocation , latitude:this.latitude,longitude:this.longitude};


				 	let apartment = {id: 0,type:this.apartmentType, numberOfRoom: this.numberOfRooms,numberOfGuest: this.numberOfGuests,location:location,dateForRenting:period,freeDateForRenting:[]
							,host:null,comments:[],pictures:this.imagesForBackend,priceForNight:this.price,checkInTime:this.checkInTime,checkOutTime:this.checkOutTime,amenities:this.selectedAmenities,status:null,reservations:[]};
				 	
	        		
				 	axios
			        .post('/apartment/add', JSON.stringify(apartment))
			        .then(response => {
			        	  window.location.href = "#/";
			        	  
			          });
				
				}
		}
	},
});


