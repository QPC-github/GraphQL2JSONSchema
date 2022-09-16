package com.ebay.graphql.transformer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ebay.graphql.model.GraphQLSchema;
import com.ebay.graphql.types.GraphQLEnum;
import com.ebay.graphql.types.GraphQLList;
import com.ebay.graphql.types.GraphQLList.Dimensionality;
import com.ebay.graphql.types.GraphQLScalar.GraphQLScalarValue;
import com.ebay.graphql.types.GraphQLObject;
import com.ebay.graphql.types.GraphQLReference;
import com.ebay.graphql.types.GraphQLScalar;
import com.ebay.graphql.types.GraphQLType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class GraphQLToJsonSchemaTest {
	
	private GraphQLToJsonSchema graphQLToJsonSchema = null;
	private JsonNodeFactory factory = new JsonNodeFactory(false);
	
	@BeforeMethod
	public void beforeEachTest() {
		GraphQLSchema schema = new GraphQLSchema();
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
	}

	@Test
	public void enumWithoutValues() throws Exception {
		
		GraphQLEnum graphQLEnum = new GraphQLEnum();
		
		JsonNode actualNode = graphQLToJsonSchema.convertEnum(graphQLEnum);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/enumWithoutValues.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void enumWithValues() throws Exception {
		
		GraphQLEnum graphQLEnum = new GraphQLEnum();
		graphQLEnum.addEnumValue("one");
		graphQLEnum.addEnumValue("TWO");

		JsonNode actualNode = graphQLToJsonSchema.convertEnum(graphQLEnum);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/enumWithValues.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void scalarBoolean() throws Exception {

		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.BOOLEAN));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarBoolean.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void scalarFloat() throws Exception {

		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.FLOAT));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarFloat.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void scalarId() throws Exception {

		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.ID));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarId.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void scalarInt() throws Exception {

		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.INT));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarInt.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void scalarString() throws Exception {
		
		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.STRING));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarString.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listBoolean() throws Exception {
		
		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.BOOLEAN), Dimensionality.SINGLE);
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listBoolean.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listFloat() throws Exception {
		
		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.FLOAT), Dimensionality.SINGLE);
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listFloat.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listInteger() throws Exception {
	
		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.INT), Dimensionality.SINGLE);
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listInteger.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listMultidimension() throws Exception {

		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.STRING), Dimensionality.MULTI);
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listMultidimension.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listObject() throws Exception {
		
		GraphQLObject graphQLObject = new GraphQLObject();
		graphQLObject.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLList graphQLList = new GraphQLList(graphQLObject, Dimensionality.SINGLE);
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listObject.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void listString() throws Exception {
		
		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.STRING), Dimensionality.SINGLE);
		JsonNode actualNode = graphQLToJsonSchema.convertList(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listString.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void referenceObject() throws Exception {
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		person.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("person", person);
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		GraphQLReference graphQLReference = new GraphQLReference("person");
		JsonNode actualNode = graphQLToJsonSchema.convertReference(graphQLReference);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/referenceObject.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@DataProvider(name = "typeDefinitionTestValues")
	public Object[][] getTypeDefinitionTestValues() {
		return new Object[][] { 
			{ new GraphQLScalar(GraphQLScalarValue.BOOLEAN), factory.arrayNode().add("boolean").add("null") },
			{ new GraphQLScalar(GraphQLScalarValue.FLOAT), factory.arrayNode().add("number").add("null") },
			{ new GraphQLScalar(GraphQLScalarValue.ID), factory.arrayNode().add("string").add("null") },
			{ new GraphQLScalar(GraphQLScalarValue.INT), factory.arrayNode().add("integer").add("null") },
			{ new GraphQLScalar(GraphQLScalarValue.STRING), factory.arrayNode().add("string").add("null") },
			{ new GraphQLList(new GraphQLScalar(GraphQLScalarValue.BOOLEAN), Dimensionality.SINGLE), factory.arrayNode().add("array").add("null") },
			{ new GraphQLObject(), factory.arrayNode().add("object").add("null") },
		};
	}
	
	@Test(dataProvider = "typeDefinitionTestValues")
	public void getTypeDefinition(GraphQLType inputType, JsonNode expectedNode) throws Exception {
		JsonNode actualNode = graphQLToJsonSchema.getTypeDefinition(inputType);
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@DataProvider(name = "nullableTypeDefinitionTestValues")
	public Object[][] getNullableTypeDefinitionTestValues() {
		return new Object[][] { 
			{ new GraphQLScalar(GraphQLScalarValue.BOOLEAN), factory.textNode("boolean") },
			{ new GraphQLScalar(GraphQLScalarValue.FLOAT), factory.textNode("number") },
			{ new GraphQLScalar(GraphQLScalarValue.ID), factory.textNode("string") },
			{ new GraphQLScalar(GraphQLScalarValue.INT), factory.textNode("integer") },
			{ new GraphQLScalar(GraphQLScalarValue.STRING), factory.textNode("string") },
			{ new GraphQLList(new GraphQLScalar(GraphQLScalarValue.BOOLEAN), Dimensionality.SINGLE), factory.textNode("array") },
			{ new GraphQLObject(), factory.textNode("object") },
		};
	}
	
	@Test(dataProvider = "nullableTypeDefinitionTestValues")
	public void getNonNullableTypeDefinition(GraphQLType inputType, JsonNode expectedNode) throws Exception {
		inputType.makeNonNullable();
		JsonNode actualNode = graphQLToJsonSchema.getTypeDefinition(inputType);
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getEnumTypeDefinition() throws Exception {
		graphQLToJsonSchema.getTypeDefinition(new GraphQLEnum());
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void getReferenceTypeDefinition() throws Exception {
		graphQLToJsonSchema.getTypeDefinition(new GraphQLReference("foo"));
	}
	
	@Test
	public void nullableBoolean() throws Exception {
		
		JsonNode actualNode = graphQLToJsonSchema.convertScalar(new GraphQLScalar(GraphQLScalarValue.BOOLEAN));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableBoolean.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableBoolean() throws Exception {
		
		GraphQLType type = new GraphQLScalar(GraphQLScalarValue.BOOLEAN);
		type.makeNonNullable();
		JsonNode actualNode = graphQLToJsonSchema.convertScalar(type);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableBoolean.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableObjectProperty() throws Exception {
		
		GraphQLObject nonNullableObject = new GraphQLObject();
		nonNullableObject.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		nonNullableObject.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));

		JsonNode actualNode = graphQLToJsonSchema.convertObject(nonNullableObject);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableObjectProperty.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableObjectProperty() throws Exception {
		
		GraphQLObject nonNullableObject = new GraphQLObject();
		nonNullableObject.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLType nonNullableInteger = new GraphQLScalar(GraphQLScalarValue.INT);
		nonNullableInteger.makeNonNullable();
		nonNullableObject.addField("age", nonNullableInteger);
		
		JsonNode actualNode = graphQLToJsonSchema.convertObject(nonNullableObject);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableObjectProperty.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableReference() throws Exception {
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		person.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("person", person);
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		GraphQLReference graphQLReference = new GraphQLReference("person");
		JsonNode actualNode = graphQLToJsonSchema.convertReference(graphQLReference);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableReference.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableReference() throws Exception {
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		person.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("person", person);
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		GraphQLReference graphQLReference = new GraphQLReference("person");
		graphQLReference.makeNonNullable();
		JsonNode actualNode = graphQLToJsonSchema.convertReference(graphQLReference);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableReference.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableNesteObjectProperty() throws Exception {
		
		GraphQLObject name = new GraphQLObject();
		name.addField("first", new GraphQLScalar(GraphQLScalarValue.STRING));
		name.addField("last", new GraphQLScalar(GraphQLScalarValue.STRING));
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", name);

		JsonNode actualNode = graphQLToJsonSchema.convertObject(person);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableNestedObjectProperty.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableNestedObjectProperty() throws Exception {
		
		GraphQLType first = new GraphQLScalar(GraphQLScalarValue.STRING);
		first.makeNonNullable();
		
		GraphQLType last = new GraphQLScalar(GraphQLScalarValue.STRING);
		last.makeNonNullable();
		
		GraphQLObject name = new GraphQLObject();
		name.addField("first", first);
		name.addField("last", last);
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", name);

		JsonNode actualNode = graphQLToJsonSchema.convertObject(person);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableNestedObjectProperty.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableList() throws Exception {
	
		GraphQLType list = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.STRING), Dimensionality.SINGLE);
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(list);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableList.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableList() throws Exception {
		
		GraphQLType scalarType = new GraphQLScalar(GraphQLScalarValue.STRING);
		scalarType.makeNonNullable();
		
		GraphQLType list = new GraphQLList(scalarType, Dimensionality.SINGLE);
		list.makeNonNullable();
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(list);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableList.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableMultidimensionalList() throws Exception {
		
		GraphQLType scalarType = new GraphQLScalar(GraphQLScalarValue.STRING);
		GraphQLType list = new GraphQLList(scalarType, Dimensionality.MULTI);
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(list);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableMultidimensionalList.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nullableMultidimensionalListButNotElementType() throws Exception {
		
		GraphQLType scalarType = new GraphQLScalar(GraphQLScalarValue.STRING);
		scalarType.makeNonNullable();
		GraphQLType list = new GraphQLList(scalarType, Dimensionality.MULTI);
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(list);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableMultidimensionalListButNotElementType.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void nonNullableMultidimensionalList() throws Exception {
		
		
		GraphQLType scalarType = new GraphQLScalar(GraphQLScalarValue.STRING);
		scalarType.makeNonNullable();
		
		GraphQLType list = new GraphQLList(scalarType, Dimensionality.MULTI);
		list.makeNonNullable();
		
		JsonNode actualNode = graphQLToJsonSchema.convertList(list);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nonNullableMultidimensionalList.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void convertModelEnum() throws Exception {
		
		GraphQLEnum graphQLEnum = new GraphQLEnum();
		graphQLEnum.addEnumValue("one");
		graphQLEnum.addEnumValue("TWO");

		JsonNode actualNode = graphQLToJsonSchema.convertModel(graphQLEnum);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/enumWithValues.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void convertModelList() throws Exception {
		
		GraphQLList graphQLList = new GraphQLList(new GraphQLScalar(GraphQLScalarValue.BOOLEAN), Dimensionality.SINGLE);
		JsonNode actualNode = graphQLToJsonSchema.convertModel(graphQLList);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/listBoolean.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void convertModelObject() throws Exception {
		
		GraphQLObject nonNullableObject = new GraphQLObject();
		nonNullableObject.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		nonNullableObject.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));

		JsonNode actualNode = graphQLToJsonSchema.convertModel(nonNullableObject);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/nullableObjectProperty.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void convertModelReference() throws Exception {
		
		GraphQLObject person = new GraphQLObject();
		person.addField("name", new GraphQLScalar(GraphQLScalarValue.STRING));
		person.addField("age", new GraphQLScalar(GraphQLScalarValue.INT));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("person", person);
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		GraphQLReference graphQLReference = new GraphQLReference("person");
		JsonNode actualNode = graphQLToJsonSchema.convertModel(graphQLReference);
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/referenceObject.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test
	public void convertModelScalar() throws Exception {
		
		JsonNode actualNode = graphQLToJsonSchema.convertModel(new GraphQLScalar(GraphQLScalarValue.INT));
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/scalarInt.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test(expectedExceptions = IllegalStateException.class)
	public void convertModelUnknownType() throws Exception {
		
		GraphQLType type = Mockito.mock(GraphQLType.class);
		graphQLToJsonSchema.convertModel(type);
	}
	
	@DataProvider(name = "scalarTypeNameData")
	public Object[][] scalarTypeNameData() {
		return new Object[][] {
			{ "boolean", new GraphQLScalar(GraphQLScalarValue.BOOLEAN) },
			{ "number", new GraphQLScalar(GraphQLScalarValue.FLOAT) },
			{ "string", new GraphQLScalar(GraphQLScalarValue.ID) },
			{ "integer", new GraphQLScalar(GraphQLScalarValue.INT) },
			{ "string", new GraphQLScalar(GraphQLScalarValue.STRING) }
		};
	}
	
	@Test(dataProvider = "scalarTypeNameData")
	public void getScalarTypeName(String expectedName, GraphQLScalar scalar) throws Exception {
		
		String actualName = graphQLToJsonSchema.getScalarTypeName(scalar);
		assertThat(actualName, is(equalTo(expectedName)));
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void getScalarTypeNameNullValue() throws Exception {
		
		graphQLToJsonSchema.getScalarTypeName(null);
	}
	
	@Test
	public void convertQuery() throws Exception {
		
		GraphQLEnum enrollmentStatusEnum = new GraphQLEnum();
		enrollmentStatusEnum.addEnumValue("ENROLLED");
		enrollmentStatusEnum.addEnumValue("NOT_ENROLLED");
		
		GraphQLReference enumRef = new GraphQLReference("EnrollmentStatusEnum");
		enumRef.makeNonNullable();
		
		GraphQLObject vaultEnrollmentStatusOutput = new GraphQLObject();
		vaultEnrollmentStatusOutput.addField("enrollmentStatus", enumRef);
		vaultEnrollmentStatusOutput.addField("didUserPassRISK", new GraphQLScalar(GraphQLScalarValue.BOOLEAN));
		vaultEnrollmentStatusOutput.addField("enrollmentTime", new GraphQLScalar(GraphQLScalarValue.STRING));
		vaultEnrollmentStatusOutput.addField("enrollmentLocales", new GraphQLList(new GraphQLScalar(GraphQLScalarValue.STRING), Dimensionality.SINGLE));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("VaultEnrollmentStatusOutput", vaultEnrollmentStatusOutput);
		schema.addType("EnrollmentStatusEnum", enrollmentStatusEnum);
		schema.addQuery("isVaultUserEnrolled(vaultUserEnrolledInput: VaultUserEnrolledInput)", new GraphQLReference("VaultEnrollmentStatusOutput"));
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		JsonNode actualNode = graphQLToJsonSchema.convertQuery("isVaultUserEnrolled(vaultUserEnrolledInput: VaultUserEnrolledInput)");
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/convertQuery.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unknownQuery() throws Exception {
		graphQLToJsonSchema.convertQuery("FOO");
	}
	
	@Test
	public void convertMutation() throws Exception {
		
		GraphQLEnum enrollmentStatusEnum = new GraphQLEnum();
		enrollmentStatusEnum.addEnumValue("ENROLLED");
		enrollmentStatusEnum.addEnumValue("NOT_ENROLLED");
		
		GraphQLReference enumRef = new GraphQLReference("EnrollmentStatusEnum");
		enumRef.makeNonNullable();
		
		GraphQLObject vaultEnrollmentStatusOutput = new GraphQLObject();
		vaultEnrollmentStatusOutput.addField("enrollmentStatus", enumRef);
		vaultEnrollmentStatusOutput.addField("didUserPassRISK", new GraphQLScalar(GraphQLScalarValue.BOOLEAN));
		vaultEnrollmentStatusOutput.addField("enrollmentTime", new GraphQLScalar(GraphQLScalarValue.STRING));
		vaultEnrollmentStatusOutput.addField("enrollmentLocales", new GraphQLList(new GraphQLScalar(GraphQLScalarValue.STRING), Dimensionality.SINGLE));
		
		GraphQLSchema schema = new GraphQLSchema();
		schema.addType("VaultEnrollmentStatusOutput", vaultEnrollmentStatusOutput);
		schema.addType("EnrollmentStatusEnum", enrollmentStatusEnum);
		schema.addMutation("unEnrollVaultUser", new GraphQLReference("VaultEnrollmentStatusOutput"));
		
		graphQLToJsonSchema = new GraphQLToJsonSchema(schema);
		
		JsonNode actualNode = graphQLToJsonSchema.convertMutation("unEnrollVaultUser");
		JsonNode expectedNode = loadResourceFile("/com/ebay/graphql/transformer/convertQuery.json");
		
		assertThat(actualNode, is(equalTo(expectedNode)));
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unknownMutation() throws Exception {
		graphQLToJsonSchema.convertMutation("FOO");
	}
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void unknownSubscription() throws Exception {
		graphQLToJsonSchema.convertSubscription("FOO");
	}
	
	private JsonNode loadResourceFile(String resource) throws URISyntaxException, IOException {
		
		URL resourceUrl = GraphQLToJsonSchemaTest.class.getResource(resource);
		File resourceFile = new File(resourceUrl.toURI());
		
		Stream<String> rawLines = Files.lines(resourceFile.toPath());
		String text = rawLines.collect(Collectors.joining("\n"));
		rawLines.close();
		
		ObjectMapper mapper = new ObjectMapper();
	    JsonNode actualObj = mapper.readTree(text);
		
		return actualObj;
	}
}
