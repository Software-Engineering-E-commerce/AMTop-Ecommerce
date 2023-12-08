import { useState, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import "./Profile.css";

const sendTokenToBackend = async (token: string) => {
    try {
        const response = await axios.post('http://localhost:9080/home/profile', { token });
        console.log('Response from backend:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error sending token to backend:', error);
    }
};


const Profile = () => {
    const location = useLocation();
    const initialToken = location.state?.userToken || '';
    
    // Define the state variables
    const [userToken, setUserToken] = useState(initialToken);
    
    // Define the state variables
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [addresses, setAddresses] = useState(['', '', '']);
    const [activeAddresses, setActiveAddresses] = useState([false, false, false]);

    const updatePersonalInfo = async () => {
        // Filter out inactive addresses
        const activeAddresses: string[] = addresses.filter((_, index) => activeAddresses[index]);
    
        const userInfo = {
            firstName,
            lastName,
            email,
            phoneNumber,
            addresses: activeAddresses,
        };
    
        try {
            const response = await axios.post('http://localhost:9080/home/updateProfile', {
                token: userToken,
                ...userInfo
            });
            console.log('Update response:', response.data);
            const { newToken, ...newData } = response.data;
    
            // Update state with the new data
            setFirstName(newData.firstName);
            setLastName(newData.lastName);
            setEmail(newData.email);
            setPhoneNumber(newData.phoneNumber);
            setAddresses(newData.addresses);
            setActiveAddresses(newData.addresses.map(() => true)); // Set all active since backend returned them
    
            if (newToken && newToken !== userToken) {
                setUserToken(newToken);
            }
        } catch (error) {
            console.error('Error updating personal info:', error);
        }
    };
    
    useEffect(() => {
        if (userToken) {
            sendTokenToBackend(userToken)
            .then(data => {
                setFirstName(data.firstName);
                setLastName(data.lastName);
                setEmail(data.email);
                setPhoneNumber(data.phoneNumber);
                setAddresses(data.addresses);
                setActiveAddresses(data.addresses.map(() => true)); // Set all active since backend returned them
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
        }
    }, []);
    


    const maxAddresses = 3; // Set the maximum number of addresses

    // Function to add a new address
    const addAddress = () => {
        const index = activeAddresses.indexOf(false);
        if (index !== -1) {
            setActiveAddresses(activeAddresses.map((active, idx) => idx === index ? true : active));
        }
    };

    // Function to delete an address
    const deleteAddress = (index: number) => {
        setActiveAddresses(activeAddresses.map((active, idx) => idx === index ? false : active));
        setAddresses(addresses.map((address, idx) => idx === index ? '' : address));
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
                    <label htmlFor="email">Email Address</label>
                    <input 
                        type="email" id="email" placeholder="Set Email Address" value={email}
                        onChange={(e) => setEmail(e.target.value)} />
                </div>

                <div className="input-group">
                    <label htmlFor="phone">Phone Number</label>
                    <input 
                        type="tel" id="phone" placeholder="Set Phone Number" value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)} />
                </div>

                <div className="addresses-container">
                    <label className="address-label">Addresses</label>
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
                    {activeAddresses.includes(false) && (
                        <button className="btn add-btn" onClick={addAddress}>Add Address</button>
                    )}
                </div>
                <button className="btn update-btn" onClick={updatePersonalInfo}>Update Personal Info</button>
            </div>
        </>
    );
};

export default Profile;