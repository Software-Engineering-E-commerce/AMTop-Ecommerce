import ProductsList from "../Components/ProductsList";

const Catalog = () => {
    return (
        <ProductsList
            isAdmin={false}
            getProducts={function (): Promise<Product[]> {
                throw new Error("Function not implemented.");
            } }
            addProduct={function (): void {
                throw new Error("Function not implemented.");
            } }
            removeProduct={function (): void {
                throw new Error("Function not implemented.");
            } }
            updateProduct={function (): void {
                throw new Error("Function not implemented.");
            } }            
        />
    );
}

export default Catalog;