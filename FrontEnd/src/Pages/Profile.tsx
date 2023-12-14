import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';
import BobUpWindow from "../Components/BobUpWindow";
import { faCircleCheck, faCircleXmark } from "@fortawesome/free-solid-svg-icons";
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
    const [email, setEmail] = useState('');
    //setEmail(jwtDecode(userToken));
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [contactPhone, setContactPhone] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [addresses, setAddresses] = useState<string[]>([]);
    const [activeAddresses, setActiveAddresses] = useState<boolean[]>([]);
    const [isCustomer, setIsCustomer] = useState(false);
    const [updateStatus, setUpdateStatus] = useState<{ success: boolean | null, message: string }>({ success: null, message: '' });

    const updatePersonalInfo = async () => {
        // Filter out inactive addresses
        setUpdateStatus({ success: null, message: '' });

        const filteredAddresses = addresses.filter((address, index) => {
            // send only addresses that are created and have value (non empty)
            return address !== '' && activeAddresses[index];
        });
        const userInfo = {
            firstName,
            lastName,
            contactPhone,
            phoneNumber,
            addresses: filteredAddresses,
        };
        console.log(userInfo);
        
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
                setUpdateStatus({ success: true, message: "Your data have been updated successfully" });
            }else{
                setUpdateStatus({ success: false, message: "Your data was not updated, Please try again" });
            }
            // Update state with the new data
        } catch (error) {
            console.error('Error updating personal info:', error);
            setUpdateStatus({ success: false, message: "An error occurred while updating your profile" });
        }
    };    

    useEffect(() => {
        if (userToken) {
          sendTokenToBackend(userToken)
            .then(data => {
              if (data !== null) {
                console.log(data)
                const decodedToken = jwtDecode(userToken);
                setEmail(decodedToken.sub || '');
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setPhoneNumber(data.phoneNumber);
                setIsCustomer(data.customer);
                setAddresses(data.addresses || []);
                setActiveAddresses(data.addresses ? data.addresses.map(() => true) : []);
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
        if (isCustomer || (!isCustomer && addresses.length === 0)) {
            setAddresses(prevAddresses => [...prevAddresses, '']);
            setActiveAddresses(prevActive => [...prevActive, true]);
        }
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
                    <label>Your Email</label>
                    <div className="email-value">{email}</div>
                </div>
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
                            type="tel" id="contactPhone" placeholder="Set contactPhone Address" value={contactPhone}
                            onChange={(e) => setContactPhone(e.target.value)} />
                    </div>
                )}

                <div className="addresses-container">
                    {addresses.length > 0 && (<h2 className="address-title">Your Addresses</h2>)}
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
                    {(isCustomer || addresses.length < 1) && (
                        <button className="btn add-btn" onClick={addAddress}>Add Address</button>
                    )}
                </div>
                <button className="btn update-btn" onClick={updatePersonalInfo}>Update Personal Info</button>
            </div>
            {updateStatus.success !== null && (
                <BobUpWindow>
                    <p style={{ color: updateStatus.success ? "green" : "red" }}>
                        <FontAwesomeIcon
                            icon={updateStatus.success ? faCircleCheck : faCircleXmark}
                            style={{ color: updateStatus.success ? "green" : "red", fontSize: "18px", marginRight: "10px" }}
                        />
                        {updateStatus.message}
                    </p>
                </BobUpWindow>
            )}
        </>
    );
};

export default Profile;