import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import BobUpWindow from "../Components/BobUpWindow";
import { faCircleCheck } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import "./Profile.css";

const sendTokenToBackend = async (token: string) => {
    const retrieveData = async () => {
        try {
            const response = await axios(
                `http://localhost:9080/home/profile`,
                {
                    method: "GET",
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                }
            );
            return response.data;
        } catch (error) {
            console.log("Error: Data not found");
            return "failed";
        }
    };
    return retrieveData();
};


const Profile = () => {
    const location = useLocation();
    const initialToken = location.state?.userToken || '';
    
    // Define the state variables
    const [userToken] = useState(initialToken);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [contactPhone, setContactPhone] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [addresses, setAddresses] = useState<string[]>([]);
    const [activeAddresses, setActiveAddresses] = useState<boolean[]>([]);
    const [isCustomer, setIsCustomer] = useState(true);
    
    const updatePersonalInfo = async () => {
        // Filter out inactive addresses
        const filteredAddresses = addresses.filter((_, index) => activeAddresses[index]);
        const userInfo = {
            firstName,
            lastName,
            contactPhone,
            phoneNumber,
            addresses: filteredAddresses,
        };
    
        try {
            const response = await axios(
              `http://localhost:9080/home/updateProfile`,
              {
                method: "POST",
                headers: {
                  'Authorization': `Bearer ${userToken}`,
                },
                data: userInfo
              }
            );
            console.log('Update response:', response.data);
            const {...newData } = response.data;
            if (response.status === 200){
                setFirstName(newData.firstName);
                setLastName(newData.lastName);
                setContactPhone(newData.contactPhone);
                setPhoneNumber(newData.phoneNumber);
                setAddresses(newData.addresses);
                setActiveAddresses(newData.addresses.map(() => true)); // Set all active since backend returned them
                <BobUpWindow>
                    <p style={{ color: "green" }}>
                    <FontAwesomeIcon
                        icon={faCircleCheck}
                        style={{
                        color: "green",
                        fontSize: "18px",
                        marginRight: "10px",
                        }}
                    />
                        Your data have updated successfully 
                    </p>
                </BobUpWindow>
            }else{
                <BobUpWindow>
                    <p style={{ color: "green" }}>
                    <FontAwesomeIcon
                        icon={faCircleCheck}
                        style={{
                        color: "red",
                        fontSize: "18px",
                        marginRight: "10px",
                        }}
                    />
                        Your data was not updated, Please try again
                    </p>
                </BobUpWindow>
            }
            // Update state with the new data
        } catch (error) {
            console.error('Error updating personal info:', error);
        }
    };    

    useEffect(() => {
        if (userToken) {
          sendTokenToBackend(userToken)
            .then(data => {
              if (data !== "failed") {
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setPhoneNumber(data.phoneNumber);
                setIsCustomer(data.isCustomer);
                if (data.addresses) {
                  setAddresses(data.addresses);
                  setActiveAddresses(data.addresses.map(() => true));
                } else {
                  // Handle case where no addresses are returned
                  setAddresses([]);
                  setActiveAddresses([]);
                }
              } else {
                console.error('Failed to fetch data');
              }
            })
            .catch(error => {
              console.error('Error fetching data:', error);
            });
        }
      }, [userToken]);
      
    

    // Function to add a new address
    const addAddress = () => {
        setAddresses(prevAddresses => [...prevAddresses, '']);
        setActiveAddresses(prevActive => [...prevActive, true]); 
    };

    // Function to delete an address
    const deleteAddress = (index: number) => {
        // Remove the address at the given index
        const updatedAddresses = addresses.filter((_, idx) => idx !== index);
        const updatedActiveAddresses = activeAddresses.filter((_, idx) => idx !== index);
        
        setAddresses(updatedAddresses);
        setActiveAddresses(updatedActiveAddresses);
    };
    return (
        <>
            <div className="profile-container">
                <h1>Personal Info</h1>
                <div className="input-group">
                    <label htmlFor="firstName">First Name</label>
                    <input 
                        type="text" id="firstName" placeholder="Set first name" value={firstName}
                        onChange={(e) => setFirstName(e.target.value)} 
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="lastName">Last Name</label>
                    <input 
                        type="text" id="lastName" placeholder="Set Last Name" value={lastName}
                        onChange={(e) => setLastName(e.target.value)} />
                </div>


                <div className="input-group">
                    <label htmlFor="phone">Phone Number</label>
                    <input 
                        type="tel" id="phone" placeholder="Set Phone Number" value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)} />
                </div>

                {!isCustomer && (
                    <div className="input-group">
                        <label htmlFor="contactPhone">Contact phone</label>
                        <input 
                            type="contactPhone" id="contactPhone" placeholder="Set contactPhone Address" value={contactPhone}
                            onChange={(e) => setContactPhone(e.target.value)} />
                    </div>
                )}

                <div className="addresses-container">
                    {addresses.length > 0 && (<h2 className="addresses-title">Your Addresses</h2>)}
                    {addresses.map((address, index) => (
                        activeAddresses[index] && (
                            <div className="address" key={index}>
                                <input
                                    type="text" placeholder="Enter address"value={address}
                                    onChange={(e) => setAddresses(addresses.map((addr, idx) => idx === index ? e.target.value : addr))}
                                />
                                <button className="btn remove-btn" onClick={() => deleteAddress(index)}>X</button>
                            </div>
                        )
                    ))}
                    {isCustomer && (
                        <button className="btn add-btn" onClick={addAddress}>Add Address</button>
                    )}
                </div>
                <button className="btn update-btn" onClick={updatePersonalInfo}>Update Personal Info</button>
            </div>
        </>
    );
};

export default Profile;